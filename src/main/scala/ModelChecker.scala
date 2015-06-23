package ctlmc
import ctlmc.ctl._
import ctlmc.mayberesult._
import ctlmc.bddgraph._

class ModelChecker(factory: GraphFactory, model: Model) {
	private def preimage(x: StateSet): StateSet = {
		model.transitions.preimage(x)
	}

	def checkAtom(paramName: String, valueName: String): MaybeResult[StateSet] = {
		for {
			(domain, paramIndex) <-
				model.parameters.get(paramName) match {
					case None => Failure("Parameter \"" + paramName + "\" not found")
					case Some(x) => Result(x)
				}
			value <- domain.get(valueName) match {
				case None => Failure(
						"Value \"" +
						valueName +
						"\" not found in domain of parameter \"" +
						paramName +
						"\""
					)
				case Some(i) => Result(i)
			}
		} yield (factory.createStateSet(paramIndex, value))
	}

	def checkEG(p: Ctl): MaybeResult[StateSet] = {
		for {
			check_p <- check(p)
			f = (x: StateSet) => {
				check_p and preimage(x)
			}
		} yield (fixpoint(f)(check_p))
	}

	def checkEU(p: Ctl, q: Ctl): MaybeResult[StateSet] = {
		for {
			check_p <- check(p)
			check_q <- check(q)
			f = (x: StateSet) => {
				check_q or (check_p and preimage(x))
			}
		} yield (fixpoint(f)(check_q))
	}

	def check(spec: Ctl): MaybeResult[StateSet] = spec match {
		case True => Result(factory.createFullStateSet())
		case False => Result(factory.createEmptyStateSet())
		case Atom(n, v) => checkAtom(n, v)
		case Not(p) => for {
				cp <- check(p)
			} yield cp.not
		case And(p, q) => for {
				cp <- check(p)
				cq <- check(q)
			} yield (cp and cq)
		case Or(p, q) => for {
				cp <- check(p)
				cq <- check(q)
			} yield (cp or cq)
		case Imply(p, q) => for {
				cp <- check(p)
				cq <- check(q)
			} yield (cp imp cq)
		case Iff(p, q) => for {
				cp <- check(p)
				cq <- check(q)
			} yield (cp biimp cq)

		// Preimage
		case EX(p) => for {
				cp <- check(p)
			} yield preimage(cp)

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

	def checkInitial(specification: Ctl): MaybeResult[Boolean] = {
		val initialState = model.states(0)
		for {
			satisfyingStates <- check(specification)
		} yield (satisfyingStates contains initialState)
	}
}
