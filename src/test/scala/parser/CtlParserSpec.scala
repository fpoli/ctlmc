package ctlmc.parser
import ctlmc.spec._
import ctlmc.mayberesult._
import ctlmc.ctl._

class CtlParserSpec extends UnitSpec {
	val parser = new CtlParser

	val goodCtl: Array[(String, Ctl)] = Array(
		("True", True),
		("False", False),
		("\"x\"=\"T\"", Atom("x", "T")),
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
		("\"not\"=\"T\"", Atom("not", "T")),
		("Not(\"and\"=\"T\")", Not(Atom("and", "T"))),
		("  AX  (  True  )  ", AX(True)),
		("  \"x\"  =  \"T\"  ", Atom("x", "T")),
		("  \"x\"  =  \"  T  \"  ", Atom("x", "  T  ")),
		("Not(EU(True, False))", Not(EU(True, False)))
	)

	val badCtl: Array[String] = Array(
		"foobar()",
		"and",
		"x",
		"\"  x  \"=\"T\""
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
