package ctlmc.parser
import ctlmc.mayberesult.{Failure => MFailure, _}
import scala.util.parsing.combinator._

trait RunParser extends RegexParsers {
	def runParser[T](parser: => Parser[T], input: => String): MaybeResult[T] = {
		parseAll(parser, input) match {
			case Success(result, _) => Result[T](result)
			case failure: NoSuccess => MFailure(failure.msg)
		}
	}
}

trait UsefulParsers extends RegexParsers {
	def parentesized[T](p: => Parser[T]): Parser[T] = "(" ~> p <~ ")"

	def parameterName = """[a-zA-Z0-9_]+""".r
	def domainName = """[a-zA-Z0-9()]+""".r
	def quotedParameterName = "\"[a-zA-Z0-9_]+\"".r ^^ (_.dropRight(1).substring(1))
	def quotedString = "\"[^\"]*\"".r ^^ (_.dropRight(1).substring(1))
	def separator = "---"
	def comma = ","

	def numericValue: Parser[Int] = """[0-9]+""".r ^^ ((x: String) => x.toInt)
}
