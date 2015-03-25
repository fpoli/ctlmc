package ctlmc
import ctlmc.bddgraph._

class Parameter(val name: String, val domain: Array[String]) {
	def getValue(v: String): Option[Int] = {
		val index = domain.indexOf(v)
		if (index == -1) {
			None
		} else {
			Some(index)
		}
	}

	override def toString(): String = {
		"<" + name + ":["+ domain.mkString(",") + "]>"
	}
}

class ParameterList(val parameters: Array[Parameter]) {
	def size(): Int = parameters.size

	def getParameter(paramName: String): Option[(Parameter, Int)] = {
		var ret: Option[(Parameter, Int)] = None
		for ((p, i) <- parameters.zipWithIndex if ret == None) {
			if (p.name == paramName && ret == None) {
				ret = Some((p, i))
			}
		}
		ret
	}

	override def toString(): String = {
		"<" + parameters.mkString(",") + ">"
	}
}

class Model(
	val parameters: ParameterList,
	val states: Array[State],
	val transitions: Graph
)
