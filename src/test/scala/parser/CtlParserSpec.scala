package ctlmc.parser
import ctlmc.spec._
import ctlmc.mayberesult._
import ctlmc.ctl._

class CtlParserSpec extends UnitSpec {
	val parser = new CtlParser

	val goodCtl: Array[(String, Ctl)] = Array(
		("True", True),
		("False", False),
		("\"x\"", Atom("x")),
		("Not(True)", Not(True)),
		("And(True, False)", And(True, False)),
		("Or(True, False)", Or(True, False)),
		("Imply(True, False)", Imply(True, False)),
		("Iff(True, False)", Iff(True, False)),
		("AX(True)", AX(True)),
		("EX(True)", EX(True)),
		("AF(True)", AF(True)),
		("EF(True)", EF(True)),
		("AG(True)", AG(True)),
		("EG(True)", EG(True)),
		("AU(True, False)", AU(True, False)),
		("EU(True, False)", EU(True, False)),
		// Special cases
		("\"not\"", Atom("not")),
		("Not(\"and\")", Not(Atom("and"))),
		("  AX  (  True  )  ", AX(True)),
		("  \"x\"  ", Atom("x")),
		("Not(EU(True, False))", Not(EU(True, False)))
	)

	val badCtl: Array[String] = Array(
		"foobar()",
		"and",
		"x",
		"\"  x  \""
	)

	for ((input, output) <- goodCtl) {
		test("Parse good CTL: " + input) {
			parser.parse(input) match {
				case Result(result) => assert(result == output)
				case Failure(msg) => fail(msg)
			}
		}
	}

	for (input <- badCtl) {
		test("Parse bad CTL: " + input) {
			parser.parse(input) match {
				case Result(result) =>
					fail("should fail, but result is " + result)
				case Failure(msg) =>
			}
		}
	}
}
