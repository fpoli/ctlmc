package ctlmc.bddgraph
import net.sf.javabdd._

class StateSet(val factory: GraphFactory, val bdd: BDD) extends FactoryElement {
	def this(factory: GraphFactory, state: State) = this(factory, {
		state.bdd
	})

	def this(factory: GraphFactory, states: Iterable[State]) = this(factory, {
		var set = factory.bddFactory.zero()
		for (s <- states) {
			set = set.or(s.bdd)
		}
		set
	})

	def this(factory: GraphFactory, i: Int, value: Boolean = true) = this(factory, {
		assert(i >= 0 && i < factory.stateVarNum)
		if (value) factory.bddFactory.ithVar(i)
		else factory.bddFactory.nithVar(i)
	})

	def contains(state: State): Boolean =
		factory.checkImplication(state.bdd, bdd)

	def not: StateSet =
		new StateSet(factory, bdd.not)

	def and(other: StateSet): StateSet =
		new StateSet(factory, bdd.and(other.bdd))

	def or(other: StateSet): StateSet =
		new StateSet(factory, bdd.or(other.bdd))

	def imp(other: StateSet): StateSet =
		new StateSet(factory, bdd.imp(other.bdd))

	def biimp(other: StateSet): StateSet =
		new StateSet(factory, bdd.biimp(other.bdd))

	override def equals(other: Any) = {
		val that = other.asInstanceOf[StateSet]
		if (that == null) false
		else {
			factory == that.factory && bdd.equals(that.bdd)
		}
	}
}