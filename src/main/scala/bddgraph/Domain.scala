package ctlmc.bddgraph
import ctlmc._
import net.sf.javabdd._

class Domain(
		val factory: GraphFactory,
		val paramName: String,
		val domainSize: Int
	) {

	val bddDomain = factory.bddFactory.extDomain(domainSize)
	bddDomain.setName(paramName)

	val bddSize = bddDomain.varNum()

	def createBDD(value: Int): BDD = {
		assert(value >= 0 && value < domainSize)
		bddDomain.ithVar(value)
	}

	def createFullBDD(): BDD = {
		bddDomain.domain()
	}
}
