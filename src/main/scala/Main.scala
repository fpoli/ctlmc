package ctlmc
import ctlmc.ctl._
import ctlmc.mayberesult._
import ctlmc.bddgraph._
import ctlmc.parser._
import scala.sys
import scala.io.Source

object Main {
	def main(args: Array[String]) = {
		println("=== CTL Model Checking ===")

		// Read arguments
		if (args.length != 2) {
			println("Usage: program (CTL specification) (path to model file)")
			println("e.g.: program 'AG(Not(And(\"c1\", \"c2\")))' \"model.fsm\"")
			sys.exit(1)
		}
		val rawSpecification = args(0)
		val modelFile = args(1)
		lazy val source = Source.fromFile(modelFile).mkString

		val graphFactory = new GraphFactory()
		val ctlParser = new CtlParser
		val fsmParser = new FsmParser(graphFactory)

		// Parse input
		val specification = ctlParser.parse(rawSpecification) match {
			case Result(result) => result
			case Failure(msg) => {
				println("[ERROR] CTL specification parsing error: " + msg)
				sys.exit(3)
			}
		}
		val model = fsmParser.parse(source) match {
			case Result(result) => result
			case Failure(msg) => {
				println("[ERROR] FSM model parsing error: " + msg)
				sys.exit(4)
			}
		}

		// Check model
		val modelChecker = new ModelChecker(graphFactory, model)
		println("Model: " + modelFile)
		println("Spec: " + specification)
		modelChecker.checkInitial(specification) match {
			case Result(res) =>
				if (res) {
					println("[OK] The model satisfies the specification")
				} else {
					println("[NO] The model does not satisfy the specification")
					sys.exit(1)
				}
			case Failure(msg) => {
				println("[ERROR] " ++ msg)
				sys.exit(2)
			}
		}
	}
}