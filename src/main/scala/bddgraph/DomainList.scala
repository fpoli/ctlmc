package ctlmc.bddgraph
import ctlmc._
import net.sf.javabdd._

class DomainList(
		val factory: GraphFactory,
		val domains: Array[Domain]
	) {

	def this(factory: GraphFactory, domains: Array[Parameter]) =
		this(factory, {for (d <- domains) yield new Domain(factory, d)})

	val bddSize = domains.foldLeft(0)(_ + _.bddSize)

	def createBDD(values: Iterable[Int]): BDD = {
		assert(values.size == domains.size)
		val res = factory.bddFactory.one()
		for ((d, v) <- domains.zip(values)) {
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
