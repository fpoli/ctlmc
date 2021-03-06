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
			println("e.g.: program 'AG(Not(And(\"c1\"=\"T\", \"c2\"=\"T\")))' \"model.fsm\"")
			sys.exit(1)
		}
		val rawSpecification = args(0)
		val modelFileName = args(1)
		val modelFile = Source.fromFile(modelFileName)
		lazy val source = try {
			modelFile.getLines mkString "\n" 
		} finally {
			modelFile.close()
		}

		val graphFactory = new GraphFactory()
		val ctlParser = new CtlParser()
		val fsmParser = new FsmParser(graphFactory)

		// Parse input
		println("(*) Parsing CTL specification " + rawSpecification + " ...")
		val specification = ctlParser.parse(rawSpecification) match {
			case Result(result) => result
			case Failure(msg) => {
				println("[ERROR] Parsing error: " + msg)
				sys.exit(3)
			}
		}
		println("(*) Parsing FSM model " + modelFileName + " ...")
		val model = fsmParser.parse(source) match {
			case Result(result) => result
			case Failure(msg) => {
				println("[ERROR] Parsing error: " + msg)
				sys.exit(4)
			}
		}
		println(" - " + model.parameters.size + " atomic propositions")
		println(" - " + model.states.size + " states")
		println(" - at least " + model.transitions.countEdges + " transitions")

		// Check model
		println("(*) Model checking...")
		val modelChecker = new ModelChecker(graphFactory, model)
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
