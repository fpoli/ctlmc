package ctlmc.bddgraph
import net.sf.javabdd._

trait BDDFactoryReference {
	val bddFactory: BDDFactory
}

trait BDDOperations extends BDDFactoryReference {
	def shiftToRight(stateVarNum: Int, bdd: BDD): BDD = {
		val pair = bddFactory.makePair()
		for (i <- 0 until stateVarNum) {
			pair.set(i, i + stateVarNum)
		}
		bdd.replace(pair)
	}

	def checkImplication(f: BDD, g: BDD): Boolean = {
		// "f implies g" if the reduced BDD for (f and not g) is always false
		f.and(g.not).isZero
	}
}

trait FactoryElement {
	val bdd: BDD
	override def toString() = bdd.toStringWithDomains()
}
