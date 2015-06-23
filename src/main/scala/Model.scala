package ctlmc
import ctlmc.bddgraph._

class Model(
	val parameters: Model.Parameters,
	val states: Array[State],
	val transitions: Graph
)

object Model {
	type ParameterName = String
	type Domain = Map[String, Int]
	type Parameters = Map[ParameterName, (Domain, Int)]
}
