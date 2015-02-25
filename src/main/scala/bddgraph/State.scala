package ctlmc.bddgraph
import net.sf.javabdd._

class State(val factory: GraphFactory, val bdd: BDD) extends FactoryElement {
	def this(factory: GraphFactory, params: Iterable[Boolean]) = this(factory, {
		assert(params.size == factory.stateVarNum)
		factory.createBDD(params, 0)
	})

	// All the parameters with the same value
	def this(factory: GraphFactory, value: Boolean) = this(factory, {
		Array.fill(factory.stateVarNum){value}
	})

	def set(i: Int, value: Boolean = true) = {
		assert(i >= 0 && i < factory.stateVarNum)
		val newbdd = bdd.exist(factory.bddFactory.makeSet(Array(i))).and(
			if (value) factory.bddFactory.ithVar(i)
			else factory.bddFactory.ithVar(i)
		)
		new State(factory, newbdd)
	}

	def toSet(): StateSet = factory.createStateSet(this)

	override def equals(other: Any) = {
		val that = other.asInstanceOf[State]
		if (that == null) false
		else {
			factory == that.factory && bdd.equals(that.bdd)
		}
	}
}
