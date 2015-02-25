package ctlmc.parser
import ctlmc.spec._
import ctlmc.mayberesult._
import ctlmc.bddgraph._

class FsmParserSpec extends UnitSpec {
	val graphFactory = new GraphFactory()
	val parser = new FsmParser(graphFactory)

	test("Parse good FSM") {
		val input = """
			a(2) Bool "f" "T"
			b(2) Bool "F" "t"
			---
			0 0
			1 0
			---
			1 2 "test"
		"""
		parser.parse(input) match {
			case Result(result) =>
			case Failure(msg) => fail(msg)
		}
	}

	test("Parse bad FSM") {
		val input = "foobar"
		parser.parse(input) match {
			case Result(result) => fail("should fail")
			case Failure(msg) =>
		}
	}
}
