package ctlmc.parser
import ctlmc.spec._
import ctlmc.mayberesult._
import ctlmc.bddgraph._
import scala.io.Source

class FsmParserSpec extends UnitSpec {
	test("Parse good FSM") {
		val graphFactory = new GraphFactory()
		val parser = new FsmParser(graphFactory)

		val input = """
			a(2) Bool "F" "T"
			a_2(4) Bool(fake) "f" "t" "tt" "ff"
			---
			0 0
			1 0
			---
			1 2 "test"
		"""
		parser.parse(input) match {
			case Result(model) => {
				assert(model.parameters.size == 2)
				assert(model.parameters("a")._2 == 0)
				assert(model.parameters("a_2")._2 == 1)
			}
			case Failure(msg) => fail(msg)
		}
	}

	test("Parse bad FSM") {
		val graphFactory = new GraphFactory()
		val parser = new FsmParser(graphFactory)

		val input = "foobar"
		parser.parse(input) match {
			case Result(model) => fail("should fail")
			case Failure(msg) =>
		}
	}

	test("Parse not-boolean-domain") {
		val graphFactory = new GraphFactory()
		val parser = new FsmParser(graphFactory)

		val testFile = getClass.getResource("/not-boolean-domain.fsm")
		val source = Source.fromURL(testFile).mkString
		parser.parse(source) match {
			case Result(model) => {
				assert(model.parameters.size == 2)
				assert(model.parameters("a")._2 == 0)
				assert(model.parameters("b")._2 == 1)
				assert(model.parameters("a")._1.keySet
					== Set("0", "1", "2", "3"))
				assert(model.parameters("a")._1.get("0") == Some(0))
			}
			case Failure(msg) => fail(msg)
		}
	}

	val testFiles = Array(
		"/brp.fsm"
	)
	for (filename <- testFiles) {
		test("Parse file " + filename) {
			val graphFactory = new GraphFactory()
			val parser = new FsmParser(graphFactory)

			val testFile = getClass.getResource(filename)
			val source = Source.fromURL(testFile).mkString
			parser.parse(source) match {
				case Result(model) =>
				case Failure(msg) => fail(msg)
			}
		}
	}
}
