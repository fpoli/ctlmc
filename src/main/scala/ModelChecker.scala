package ctlmc
import ctlmc.ctl._
import ctlmc.mayberesult._
import ctlmc.bddgraph._

class ModelCheckerException(val msg: String) extends Throwable

class ModelChecker(factory: GraphFactory, model: Model) {
	def checkAtom(paramName: String, valueName: String): StateSet = {
		val (param, paramIndex) = model.parameters.getParameter(paramName) match {
			case None =>
				throw new ModelCheckerException(
					"Parameter \"" + paramName + "\" not found"
				)
			case Some(x) => x
		}
		val value = param.getValue(valueName) match {
			case None =>
				throw new ModelCheckerException(
					"Value \"" + valueName + "\" not found in domain of parameter \"" + paramName + "\""
				)
			case Some(i) => i
		}
		factory.createStateSet(paramIndex, value)
	}

	def preimage(x: StateSet): StateSet = {
		model.transitions.preimage(x)
	}

	def checkEG(p: Ctl): StateSet = {
		val check_p = check(p)
		val f = (x: StateSet) => {
			check_p.and(preimage(x))
		}
		fixpoint(f)(check_p)
	}

	def checkEU(p: Ctl, q: Ctl): StateSet = {
		val check_p = check(p)
		val check_q = check(q)
		val f = (x: StateSet) => {
			check_q.or(check_p.and(preimage(x)))
		}
		fixpoint(f)(check_q)
	}

	def check(spec: Ctl): StateSet = {
		spec match {
			case True => factory.createFullStateSet()
			case False => factory.createEmptyStateSet()
			case Atom(n, v) => checkAtom(n, v)
			case Not(p) => check(p).not
			case And(p, q) => check(p) and check(q)
			case Or(p, q) => check(p) or check(q)
			case Imply(p, q) => check(p) imp check(q)
			case Iff(p, q) => check(p) biimp check(q)

			// Preimage
			case EX(p) => preimage(check(p))

			// Fixpoint
			case EG(p) => checkEG(p)
			case EU(p, q) => checkEU(p, q)

			// Syntactic sugar
			case AU(p, q) => check(And(
				Not(EU(Not(q), And(Not(p), Not(q)))),
				Not(EG(Not(q)))
			))
			case EF(p) => check(EU(True, p))
			case AG(p) => check(Not(EF(Not(p))))
			case AF(p) => check(Not(EG(Not(p))))
			case AX(p) => check(Not(EX(Not(p))))
		}
	}

	def checkInitial(specification: Ctl): MaybeResult[Boolean] = {
		try {
			val satisfyingStates = check(specification)
			val initialState = model.states(0)

			/*if (model.states.size < 100) {
				println("States:")
				for ((s,i) <- model.states.zipWithIndex) {
					if (satisfyingStates contains s) {
						println("  " + i)
					}
				}
			}*/

			Result(satisfyingStates contains initialState)
		} catch {
			case e: ModelCheckerException => Failure(e.msg)
		}
	}
}
