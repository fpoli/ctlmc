package ctlmc.bddgraph
import ctlmc.spec._
import ctlmc._

class GraphFactorySpec extends UnitSpec {
	test("Creation") {
		val factory = new GraphFactory()
	}
}

class GraphSpec extends UnitSpec {
	val factory = new GraphFactory()
	factory.setParameters(Array(
		("v1", (Array("F", "T").zipWithIndex.toMap, 0)),
		("v2", (Array("F", "T").zipWithIndex.toMap, 1)),
		("v3", (Array("F", "T").zipWithIndex.toMap, 2)),
		("v4", (Array("F", "T").zipWithIndex.toMap, 3)),
		("v5", (Array("F", "T").zipWithIndex.toMap, 4))
	).toMap)
	val params = Array[Int](0, 0, 0, 0, 0)

	test("Single var State comparison, positive") {
		assert(factory.createState(params).set(1, 1)
			== factory.createState(params).set(1, 1))
	}

	test("Single var State comparison, negative") {
		assert(factory.createState(params).set(1, 1)
			!= factory.createState(params).set(2, 1))
	}

	test("Full StateSet comparison") {
		assert(factory.createFullStateSet() == factory.createFullStateSet())
	}

	test("Empty StateSet comparison") {
		assert(factory.createEmptyStateSet() == factory.createEmptyStateSet())
	}

	test("Custom StateSet comparison, positive 1") {
		val s0 = factory.createState(params).set(1, 1)
		assert(factory.createStateSet(s0) == factory.createStateSet(s0))
	}

	test("Custom StateSet comparison, positive 2") {
		val s0 = factory.createState(params).set(0, 1)
		val s1 = factory.createState(params).set(1, 1)
		assert(factory.createStateSet(Array(s0, s1))
			== factory.createStateSet(Array(s1, s0)))
	}

	test("Custom StateSet comparison, negative") {
		val s0 = factory.createState(params).set(0, 1)
		val s1 = factory.createState(params).set(1, 1)
		assert(factory.createStateSet(s0) != factory.createStateSet(s1))
	}

	test("Graph size") {
		val s0 = factory.createState(params).set(0, 1)
		val s1 = factory.createState(params).set(1, 1)
		val s2 = factory.createState(params).set(2, 1)
		val graph = factory.createGraph(Array(
			factory.createEdge(s0, s1),
			factory.createEdge(s1, s2),
			factory.createEdge(s2, s0)
		))
		assert(graph.countEdges == 3)
	}

	test("Preimage, segment") {
		val s0 = factory.createState(params).set(0, 1)
		val s1 = factory.createState(params).set(1, 1)
		val graph = factory.createGraph(Array(
			factory.createEdge(s0, s1)
		))
		val set = factory.createStateSet(s1)
		val pre = graph.preimage(set)
		assert(pre == factory.createStateSet(s0))
	}

	test("Preimage, line") {
		val s0 = factory.createState(params).set(0, 1)
		val s1 = factory.createState(params).set(1, 1)
		val s2 = factory.createState(params).set(2, 1)
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
		val s0 = factory.createState(params).set(0, 1)
		val s1 = factory.createState(params).set(1, 1)
		val s2 = factory.createState(params).set(2, 1)
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
