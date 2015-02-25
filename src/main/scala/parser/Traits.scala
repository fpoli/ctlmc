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
	def quoted[T](p: => Parser[T]): Parser[T] = "\"" ~> p <~ "\""
	
	def nonQuotes = """[^"]*""".r
	def alphaNumeric = """[a-zA-Z0-9]+""".r
	def quotedAlphaNumeric = "\"[a-zA-Z0-9]+\"".r ^^ { _.dropRight(1).substring(1) }
	def falseString = "F" | "f"
	def trueString = "T" | "t"
	def separator = "---"
	def comma = ","

	def numericValue: Parser[Int] = """[0-9]+""".r ^^ ((x: String) => x.toInt)
	def booleanValue: Parser[Boolean] = ("0" ^^ (_ => false)) | ("1" ^^ (_ => true))
}
