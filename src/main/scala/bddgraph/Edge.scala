package ctlmc.bddgraph
import net.sf.javabdd._

class Edge(val factory: GraphFactory, val bdd: BDD) extends FactoryElement {
	def this(factory: GraphFactory, start: State, end: State) = this(factory, {
		start.bdd.and(factory.shiftToRight(factory.stateVarNum, end.bdd))
	})
}
