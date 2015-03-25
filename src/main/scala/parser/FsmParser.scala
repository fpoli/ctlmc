package ctlmc.parser
import ctlmc._
import ctlmc.bddgraph._
import ctlmc.mayberesult.{Failure => MFailure, _}
import scala.util.parsing.combinator._

class FsmParser(factory: GraphFactory) extends RegexParsers
	with UsefulParsers with RunParser {

	// 1. Parameters

	def valueSeq(valNum: Int): Parser[Array[String]] =
		repN(valNum, quotedString) ^^ (_.toArray)
	def parameter: Parser[Parameter] =
		parameterName >> (param =>
			parentesized(numericValue) >> (valNum =>
				domainName ~> valueSeq(valNum) ^^ (values =>
					new Parameter(param, values)
				)
			)
		)
	def parameterSeq: Parser[ParameterList] =
		rep1(parameter) ^^ (x => new ParameterList(x.toArray))

	// 2. States

	def state(varNum: Int): Parser[State] =
		repN(varNum, numericValue) ^^ (factory.createState(_))
	def stateSeq(varNum: Int): Parser[Array[State]] =
		rep1(state(varNum)) ^^ (_.toArray)

	// 3. Transitions

	def transition(states: Array[State]): Parser[Edge] =
		numericValue ~ numericValue <~ quotedString ^^ (x =>
			factory.createEdge(states(x._1 - 1), states(x._2 - 1))
		)
	def transitionSeq(states: Array[State]): Parser[Graph] =
		rep(transition(states)) ^^ (factory.createGraph(_))

	// Expression

	def expr: Parser[Model] =
		parameterSeq <~ separator >> (params => {
			factory.setParameters(params.parameters)
			stateSeq(params.size) <~ separator >> (states =>
				transitionSeq(states) ^^ (g =>
					new Model(params, states, g)
				)
			)
		})

	def parse(input: => String): MaybeResult[Model] =
		runParser(expr, input)
}
