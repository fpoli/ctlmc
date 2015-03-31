package ctlmc.bddgraph
import net.sf.javabdd._

class Graph(val factory: GraphFactory, val bdd: BDD) extends FactoryElement {
	def this(factory: GraphFactory, edges: Iterable[Edge]) = this(factory, {
		var graph = factory.bddFactory.zero()
		for (e <- edges) {
			graph = graph.or(e.bdd)
		}
		graph
	})

	def preimage(set: StateSet): StateSet = {
		val endVars = (factory.stateVarNum until 2*factory.stateVarNum).toArray
		val endVarSet = factory.bddFactory.makeSet(endVars)
		val endStateSet = factory.shiftToRight(factory.stateVarNum, set.bdd)
		val validTransitions = bdd.and(endStateSet)
		val startStateSet = validTransitions.exist(endVarSet)
		new StateSet(factory, startStateSet)
	}

	def countEdges(): Int = {
		bdd.pathCount().toInt
	}
}
