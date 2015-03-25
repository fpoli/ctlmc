package ctlmc.bddgraph
import ctlmc._
import net.sf.javabdd._

class Domain(
		val factory: GraphFactory,
		val name: String,
		val domainSize: Int
	) {

	def this(factory: GraphFactory, param: Parameter) =
		this(factory, param.name, param.domain.size)

	val bddDomain = factory.bddFactory.extDomain(domainSize)
	bddDomain.setName(name)

	val bddSize = bddDomain.varNum()

	def createBDD(value: Int): BDD = {
		assert(value >= 0 && value < domainSize)
		bddDomain.ithVar(value)
	}

	def createFullBDD(): BDD = {
		bddDomain.domain()
	}
}
