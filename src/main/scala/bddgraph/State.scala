package ctlmc.bddgraph
import net.sf.javabdd._

class State(val factory: GraphFactory, val bdd: BDD) extends FactoryElement {
	def this(factory: GraphFactory, params: Iterable[Int]) = this(factory, {
		assert(params.size == factory.numParameters)
		factory.domainList.createBDD(params)
	})

	def set(param: Int, value: Int) = {
		assert(param >= 0 && param < factory.numParameters)
		val domain = factory.domainList.getDomain(param)
		assert(value >= 0 && value < domain.domainSize)
		val newbdd = bdd.exist(domain.bddDomain.set).and(
			domain.createBDD(value)
		)
		new State(factory, newbdd)
	}

	def toSet(): StateSet = factory.createStateSet(this)

	override def equals(other: Any) = other match {
		case that: State => factory == that.factory && bdd.equals(that.bdd)
		case _ => false
	}
}
