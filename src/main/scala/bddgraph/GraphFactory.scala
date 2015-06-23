package ctlmc.bddgraph
import ctlmc._
import net.sf.javabdd._

class GraphFactory extends BDDFactoryReference with BDDOperations {
	val bddFactory = BDDFactory.init("java", 1000, 1000)
	var domainList: DomainList = new DomainList(this, Array[Domain]())

	// Hide default listeners
	def bddCallback() = {}
	bddFactory.registerGCCallback(this, getClass.getDeclaredMethod("bddCallback"))
	bddFactory.registerReorderCallback(this, getClass.getDeclaredMethod("bddCallback"))
	bddFactory.registerResizeCallback(this, getClass.getDeclaredMethod("bddCallback"))

	def numParameters(): Int = domainList.size
	def stateVarNum(): Int = domainList.bddSize

	def setParameters(parameters: Model.Parameters): Unit = {
		assert(numParameters == 0, "Parameters already initialized")
		domainList = new DomainList(this, {
			for ((param, (domain, index)) <- parameters.toSeq.sortBy(_._2._2))
			yield new Domain(this, param, domain.size)
		}.toIndexedSeq)
		bddFactory.setVarNum(2*domainList.bddSize)
	}

	def createState(params: Iterable[Int]): State =
		new State(this, params)

	def createStateSet(state: State): StateSet =
		new StateSet(this, state)
	def createStateSet(states: Iterable[State]): StateSet =
		new StateSet(this, states)
	def createStateSet(param: Int, value: Int): StateSet =
		new StateSet(this, param, value)
	def createFullStateSet(): StateSet =
		new StateSet(this, domainList.createFullBDD) // This is not the one() BDD
	def createEmptyStateSet(): StateSet =
		new StateSet(this, bddFactory.zero)
	
	def createEdge(start: State, end: State): Edge =
		new Edge(this, start, end)
	
	def createGraph(edges: Iterable[Edge]): Graph =
		new Graph(this, edges)
}
