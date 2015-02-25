package ctlmc
import ctlmc.bddgraph._

class Model(
	val parameters: IndexedSeq[String],
	val states: IndexedSeq[State],
	val transitions: Graph
)
