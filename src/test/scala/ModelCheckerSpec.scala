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
		Not(Atom("a", "T")),
		Not(Atom("b", "T")),
		Or(Atom("a", "T"), Not(Atom("b", "T"))),
		And(Not(Atom("a", "T")), Not(Atom("b", "T"))),
		// Preimage (AX, EX)
		AX(Or(Atom("a", "T"), Atom("b", "T"))),
		AX(AX(AX(Or(Atom("a", "T"), Atom("b", "T"))))),
		AX(AX(Or(
			And(Atom("a", "T"), Atom("b", "T")),
			And(Not(Atom("a", "T")), Not(Atom("b", "T")))
		))),
		EX(Atom("a", "T")),
		EX(Atom("b", "T")),
		EX(Not(Atom("a", "T"))),
		EX(Not(Atom("b", "T"))),
		// Fixpoint
		EG(Not(Atom("a", "T"))),
		EG(Not(Atom("b", "T"))),
		EG(Or(Atom("a", "T"), EX(Atom("a", "T"))))
	),
	Array[Ctl](
		False,
		Atom("a", "T"),
		Atom("b", "T"),
		And(Not(Atom("a", "T")), Atom("b", "T")),
		// Preimage (AX, EX)
		AX(AX(Or(Atom("a", "T"), Atom("b", "T")))),
		And(
			And(Not(Atom("a", "T")), Not(Atom("b", "T"))),
			EX(And(Atom("a", "T"), Atom("b", "T")))
		),
		EX(And(Atom("a", "T"), Atom("b", "T"))),
		EX(And(Not(Atom("a", "T")), Not(Atom("b", "T")))),
		// Fixpoint
		EG(Atom("a", "T")),
		EG(Atom("b", "T")),
		AG(Not(Atom("a", "T"))),
		AG(Not(Atom("b", "T"))),
		EF(And(
			And(Atom("a", "T"), Atom("b", "T")),
			EX(And(Not(Atom("a", "T")), Not(Atom("b", "T"))))
		))
	)
)

class ModelCheckerTriangleSpec extends ModelCheckerSourceSpec(
	"/triangle.fsm",
	Array[Ctl](
		// test Not(And(Atom("a", "T"), Atom("b", "T")))
		Not(And(Atom("a", "T"), Atom("b", "T"))),
		AX(Not(And(Atom("a", "T"), Atom("b", "T")))),
		AX(AX(Not(And(Atom("a", "T"), Atom("b", "T"))))),
		EG(Not(And(Atom("a", "T"), Atom("b", "T")))),
		AG(Not(And(Atom("a", "T"), Atom("b", "T")))),
		// test Imply(Atom("a", "T"), AX(Atom("b", "T")))
		Imply(Atom("a", "T"), AX(Atom("b", "T"))),
		AX(Imply(Atom("a", "T"), AX(Atom("b", "T")))),
		AX(AX(Imply(Atom("a", "T"), AX(Atom("b", "T"))))),
		EG(Imply(Atom("a", "T"), AX(Atom("b", "T")))),
		AG(Imply(Atom("a", "T"), AX(Atom("b", "T")))),
		// Test Iff
		AG(Iff(Atom("c", "T"), AX(Atom("a", "T"))))
	),
	Array[Ctl](
		
	)
)

class ModelCheckerMutualExclusionSpec extends ModelCheckerSourceSpec(
	"/mutual-exclusion.fsm",
	Array[Ctl](
		// === Mutual exclusion (safety)
		// There is no reachable state in which C1 C2 holds
		AG(Not(And(Atom("c1", "T"), Atom("c2", "T")))),
		// === Liveness
		// Every path starting from each state where T1 holds
		// passes through a state where C1 holds
		Imply(AG(Atom("t1", "T")), AF(Atom("c1", "T"))),
		// === Blocking
		// From each state where N1 holds there is a path leading to
		// a state where T1 holds
		AG(Imply(Atom("n1", "T"), EF(Atom("t1", "T")))),
		// Test AU
		AG(AU(Not(Atom("c2", "T")), Not(Atom("c1", "T"))))
	),
	Array[Ctl](
		// === Fairness
		// In the initial state, there is an infinite cyclic solution in
		// which C1 never holds
		AG(AF(Atom("c1", "T")))
	)
)
