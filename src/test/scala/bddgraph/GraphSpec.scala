package ctlmc.bddgraph
import ctlmc.spec._

class GraphSpec extends UnitSpec {
	val factory = new GraphFactory()
	factory.setStateVarNum(5)

	test("Single var State creation") {
		val s0 = factory.createState(false).set(0)
		var params = Array.fill(factory.stateVarNum){false}
		params(0) = true
		val s1 = factory.createState(params)
		assert(s0 == s1)
	}

	test("Single var State comparison, positive") {
		assert(factory.createState(false).set(1)
			== factory.createState(false).set(1))
	}

	test("Single var State comparison, negative") {
		assert(factory.createState(false).set(1)
			!= factory.createState(false).set(2))
	}

	test("Full StateSet comparison") {
		assert(factory.createFullStateSet() == factory.createFullStateSet())
	}

	test("Empty StateSet comparison") {
		assert(factory.createEmptyStateSet() == factory.createEmptyStateSet())
	}

	test("Custom StateSet comparison, positive 1") {
		val s0 = factory.createState(false).set(1)
		assert(factory.createStateSet(s0) == factory.createStateSet(s0))
	}

	test("Custom StateSet comparison, positive 2") {
		val s0 = factory.createState(false).set(0)
		val s1 = factory.createState(false).set(1)
		assert(factory.createStateSet(Array(s0, s1))
			== factory.createStateSet(Array(s1, s0)))
	}

	test("Custom StateSet comparison, negative") {
		val s0 = factory.createState(false).set(0)
		val s1 = factory.createState(false).set(1)
		assert(factory.createStateSet(s0) != factory.createStateSet(s1))
	}

	test("Preimage, segment") {
		val s0 = factory.createState(false).set(0)
		val s1 = factory.createState(false).set(1)
		val graph = factory.createGraph(Array(
			factory.createEdge(s0, s1)
		))
		val set = factory.createStateSet(s1)
		val pre = graph.preimage(set)
		assert(pre == factory.createStateSet(s0))
	}

	test("Preimage, line") {
		val s0 = factory.createState(false).set(0)
		val s1 = factory.createState(false).set(1)
		val s2 = factory.createState(false).set(2)
		val graph = factory.createGraph(Array(
			factory.createEdge(s0, s1),
			factory.createEdge(s1, s2),
			factory.createEdge(s2, s0)
		))
		val set = factory.createStateSet(s2)
		val pre = graph.preimage(set)
		assert(pre == factory.createStateSet(s1))
	}

	test("Preimage, triangle") {
		val s0 = factory.createState(false).set(0)
		val s1 = factory.createState(false).set(1)
		val s2 = factory.createState(false).set(2)
		val graph = factory.createGraph(Array(
			factory.createEdge(s0, s1),
			factory.createEdge(s1, s2),
			factory.createEdge(s2, s0)
		))
		val set = factory.createStateSet(s0)
		val pre = graph.preimage(set)
		assert(pre == factory.createStateSet(s2))
	}
}
