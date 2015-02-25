package ctlmc.ctl
import ctlmc.spec._

class CtlCreationSpec extends UnitSpec {
	test("Atom") {
		Atom("x")
	}
	test("Not") {
		Not(Atom("x"))
	}
	test("And") {
		And(Atom("x"), Atom("y"))
	}
	test("AU") {
		AU(AX(And(True, Atom("y"))), Not(Atom("x")))
	}
}

class CtlComparisonSpec extends UnitSpec {
	test("Atom, positive") {
		assert(Atom("x") == Atom("x"))
	}
	test("Atom, negative") {
		assert(Atom("x") != Atom("y"))
	}
	test("Not, positive") {
		assert(Not(Atom("x")) == Not(Atom("x")))
	}
	test("Not, negative") {
		assert(Not(Atom("x")) != Not(Atom("y")))
	}
	test("And, positive") {
		assert(And(Atom("x"), Atom("x")) == And(Atom("x"), Atom("x")))
	}
	test("And, negative") {
		assert(And(Atom("x"), Atom("x")) != And(Atom("y"), Atom("x")))
		assert(And(Atom("x"), Atom("x")) != And(Atom("x"), Atom("y")))
	}
}

class CtlToStringSpec extends UnitSpec {
	test("Atom to string") {
		assert(Atom("x").toString == "\"x\"")
	}
	test("Not to string") {
		assert(Not(Atom("x")).toString == "Not(\"x\")")
	}
	test("And to string") {
		assert(And(Atom("x"), Atom("y")).toString == "And(\"x\",\"y\")")
	}
}
