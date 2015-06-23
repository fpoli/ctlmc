package ctlmc.bddgraph
import ctlmc._
import net.sf.javabdd._

class DomainList(
		val factory: GraphFactory,
		val domains: IndexedSeq[Domain]
	) {

	val bddSize = domains.foldLeft(0)(_ + _.bddSize)

	def createBDD(values: Iterable[Int]): BDD = {
		assert(values.size == domains.size)
		val res = factory.bddFactory.one()
		for ((d, v) <- domains.zip(values)) {
			assert(v >= 0 && v < d.domainSize, "values " + values)
			res.andWith(d.createBDD(v))
		}
		res
	}

	def size(): Int =
		domains.size

	def getDomain(i: Int) = {
		domains(i)
	}

	def createFullBDD(): BDD = {
		val res = factory.bddFactory.one()
		for (d <- domains) {
			res.andWith(d.createFullBDD())
		}
		res
	}
}
