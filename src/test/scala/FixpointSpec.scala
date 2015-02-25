package ctlmc
import ctlmc.spec._

class FixpointSpec extends UnitSpec {
	test("Fixpoint of x => 10") {
		def f(x: Int): Int = 10
		val res = fixpoint(f)(0)
		assert(res == 10)
	}
	test("Fixpoint of x => min(20, x+1), from 20") {
		def f(x: Int): Int = math.min(20, x+1)
		val res = fixpoint(f)(20)
		assert(res == 20)
	}
	test("Fixpoint of x => min(30, x+1), from 0") {
		def f(x: Int): Int = math.min(30, x+1)
		val res = fixpoint(f)(0)
		assert(res == 30)
	}
}
