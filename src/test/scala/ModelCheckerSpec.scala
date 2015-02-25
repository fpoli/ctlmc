package ctlmc
import ctlmc.spec._
import ctlmc.parser._
import ctlmc.ctl._
import ctlmc.mayberesult._
import ctlmc.bddgraph._
import scala.io.Source

abstract class ModelCheckerSourceSpec(
		resource: String,
		trueSpec: Iterable[Ctl],
		falseSpec: Iterable[Ctl]
	) extends UnitSpec {

	val testFile = getClass.getResource(resource)
	val source = Source.fromURL(testFile).mkString
	val graphFactory = new GraphFactory()
	val parser = new FsmParser(graphFactory)
	val model = parser.parse(source) match {
		case Result(result) => result
		case Failure(msg) => {
			println("Parsing error: " + msg)
			sys.exit(1)
		}
	}
	val checker = new ModelChecker(graphFactory, model)

	for (spec <- trueSpec) {
		test("True CTL formula " + spec.toString()) {
			assert(checker.checkInitial(spec) == Result(true))
		}
	}

	for (spec <- falseSpec) {
		test("False CTL formula " + spec.toString()) {
			assert(checker.checkInitial(spec) == Result(false))
		}
	}
}

class ModelCheckerTwoSwitchesSpec extends ModelCheckerSourceSpec(
	"/two-switches.fsm",
	Array[Ctl](
		True,
		Not(Atom("a")),
		Not(Atom("b")),
		Or(Atom("a"), Not(Atom("b"))),
		And(Not(Atom("a")), Not(Atom("b"))),
		// Preimage (AX, EX)
		AX(Or(Atom("a"), Atom("b"))),
		AX(AX(AX(Or(Atom("a"), Atom("b"))))),
		AX(AX(Or(
			And(Atom("a"), Atom("b")),
			And(Not(Atom("a")), Not(Atom("b")))
		))),
		EX(Atom("a")),
		EX(Atom("b")),
		EX(Not(Atom("a"))),
		EX(Not(Atom("b"))),
		// Fixpoint
		EG(Not(Atom("a"))),
		EG(Not(Atom("b"))),
		EG(Or(Atom("a"), EX(Atom("a"))))
	),
	Array[Ctl](
		False,
		Atom("a"),
		Atom("b"),
		And(Not(Atom("a")), Atom("b")),
		// Preimage (AX, EX)
		AX(AX(Or(Atom("a"), Atom("b")))),
		And(
			And(Not(Atom("a")), Not(Atom("b"))),
			EX(And(Atom("a"), Atom("b")))
		),
		EX(And(Atom("a"), Atom("b"))),
		EX(And(Not(Atom("a")), Not(Atom("b")))),
		// Fixpoint
		EG(Atom("a")),
		EG(Atom("b")),
		AG(Not(Atom("a"))),
		AG(Not(Atom("b"))),
		EF(And(
			And(Atom("a"), Atom("b")),
			EX(And(Not(Atom("a")), Not(Atom("b"))))
		))
	)
)

class ModelCheckerTriangleSpec extends ModelCheckerSourceSpec(
	"/triangle.fsm",
	Array[Ctl](
		// test Not(And(Atom("a"), Atom("b")))
		Not(And(Atom("a"), Atom("b"))),
		AX(Not(And(Atom("a"), Atom("b")))),
		AX(AX(Not(And(Atom("a"), Atom("b"))))),
		EG(Not(And(Atom("a"), Atom("b")))),
		AG(Not(And(Atom("a"), Atom("b")))),
		// test Imply(Atom("a"), AX(Atom("b")))
		Imply(Atom("a"), AX(Atom("b"))),
		AX(Imply(Atom("a"), AX(Atom("b")))),
		AX(AX(Imply(Atom("a"), AX(Atom("b"))))),
		EG(Imply(Atom("a"), AX(Atom("b")))),
		AG(Imply(Atom("a"), AX(Atom("b")))),
		// Test Iff
		AG(Iff(Atom("c"), AX(Atom("a"))))
	),
	Array[Ctl](
		
	)
)

class ModelCheckerMutualExclusionSpec extends ModelCheckerSourceSpec(
	"/mutual-exclusion.fsm",
	Array[Ctl](
		// === Mutual exclusion (safety)
		// There is no reachable state in which C1 C2 holds
		AG(Not(And(Atom("c1"), Atom("c2")))),
		// === Liveness
		// Every path starting from each state where T1 holds
		// passes through a state where C1 holds
		Imply(AG(Atom("t1")), AF(Atom("c1"))),
		// === Blocking
		// From each state where N1 holds there is a path leading to
		// a state where T1 holds
		AG(Imply(Atom("n1"), EF(Atom("t1")))),
		// Test AU
		AG(AU(Not(Atom("c2")), Not(Atom("c1"))))
	),
	Array[Ctl](
		// === Fairness
		// In the initial state, there is an infinite cyclic solution in
		// which C1 never holds
		AG(AF(Atom("c1")))
	)
)
