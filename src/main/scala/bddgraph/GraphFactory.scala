package ctlmc.bddgraph
import net.sf.javabdd._

class GraphFactory extends BDDFactoryReference with BDDOperations {
	val bddFactory = BDDFactory.init("java", 1000, 1000)

	def setStateVarNum(n: Int): Unit = bddFactory.setVarNum(2*n)
	def stateVarNum(): Int = bddFactory.varNum() / 2

	def createState(params: Iterable[Boolean]): State =
		new State(this, params)
	def createState(value: Boolean): State =
		new State(this, value) // All the parameters have the same value
	def createStateSet(state: State): StateSet =
		new StateSet(this, state)
	def createStateSet(states: Iterable[State]): StateSet =
		new StateSet(this, states)
	def createStateSet(i: Int, value: Boolean = true): StateSet =
		new StateSet(this, i, value)
	def createFullStateSet(): StateSet =
		new StateSet(this, bddFactory.one)
	def createEmptyStateSet(): StateSet =
		new StateSet(this, bddFactory.zero)
	def createEdge(start: State, end: State): Edge =
		new Edge(this, start, end)
	def createGraph(edges: Iterable[Edge]): Graph =
		new Graph(this, edges)
}
