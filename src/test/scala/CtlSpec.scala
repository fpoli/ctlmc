package ctlmc.ctl
import ctlmc.spec._

class CtlCreationSpec extends UnitSpec {
	test("Atom") {
		Atom("x", "T")
	}
	test("Not") {
		Not(Atom("x", "T"))
	}
	test("And") {
		And(Atom("x", "T"), Atom("y", "T"))
	}
	test("AU") {
		AU(AX(And(True, Atom("y", "T"))), Not(Atom("x", "T")))
	}
}

class CtlComparisonSpec extends UnitSpec {
	test("Atom, positive") {
		assert(Atom("x", "T") == Atom("x", "T"))
	}
	test("Atom, negative") {
		assert(Atom("x", "T") != Atom("y", "T"))
	}
	test("Not, positive") {
		assert(Not(Atom("x", "T")) == Not(Atom("x", "T")))
	}
	test("Not, negative") {
		assert(Not(Atom("x", "T")) != Not(Atom("y", "T")))
	}
	test("And, positive") {
		assert(And(Atom("x", "T"), Atom("x", "T")) == And(Atom("x", "T"), Atom("x", "T")))
	}
	test("And, negative") {
		assert(And(Atom("x", "T"), Atom("x", "T")) != And(Atom("y", "T"), Atom("x", "T")))
		assert(And(Atom("x", "T"), Atom("x", "T")) != And(Atom("x", "T"), Atom("y", "T")))
	}
}

class CtlToStringSpec extends UnitSpec {
	test("Atom to string") {
		assert(Atom("x", "T").toString == "\"x\"=\"T\"")
	}
	test("Not to string") {
		assert(Not(Atom("x", "T")).toString == "Not(\"x\"=\"T\")")
	}
	test("And to string") {
		assert(And(Atom("x", "T"), Atom("y", "T")).toString == "And(\"x\"=\"T\",\"y\"=\"T\")")
	}
}
