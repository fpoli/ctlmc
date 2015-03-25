package ctlmc.parser
import ctlmc.ctl._
import ctlmc.bddgraph._
import ctlmc.mayberesult.{Failure => MFailure, _}
import scala.util.parsing.combinator._

class CtlParser extends RegexParsers with UsefulParsers with RunParser {
	def exprTrue = "(?i)True".r ^^ { _ => True }
	def exprFalse = "(?i)False".r ^^ { _ => False }
	def exprAtom = quotedParameterName ~ ("=" ~> quotedString) ^^ (
			x => Atom(x._1, x._2)
		)

	def exprOp(name: String): Parser[Ctl] =
		("(?i)" + name).r ~> parentesized(expr)

	def exprOp2(name: String): Parser[(Ctl, Ctl)] =
		("(?i)" + name).r ~> parentesized((expr <~ comma) ~ expr) ^^ {
			x => (x._1, x._2)
		}

	def exprNot   = exprOp("Not")    ^^ { x           => Not(x)      }
	def exprAnd   = exprOp2("And")   ^^ { case (x, y) => And(x, y)   }
	def exprOr    = exprOp2("Or")    ^^ { case (x, y) => Or(x, y)    }
	def exprImply = exprOp2("Imply") ^^ { case (x, y) => Imply(x, y) }
	def exprIff   = exprOp2("Iff")   ^^ { case (x, y) => Iff(x, y)   }
	def exprAX    = exprOp("AX")     ^^ { x           => AX(x)       }
	def exprEX    = exprOp("EX")     ^^ { x           => EX(x)       }
	def exprAF    = exprOp("AF")     ^^ { x           => AF(x)       }
	def exprEF    = exprOp("EF")     ^^ { x           => EF(x)       }
	def exprAG    = exprOp("AG")     ^^ { x           => AG(x)       }
	def exprEG    = exprOp("EG")     ^^ { x           => EG(x)       }
	def exprAU    = exprOp2("AU")    ^^ { case (x, y) => AU(x, y)     }
	def exprEU    = exprOp2("EU")    ^^ { case (x, y) => EU(x, y)     }

	def expr: Parser[Ctl] = parentesized(expr) |
		exprTrue |
		exprFalse |
		exprAtom |
		exprNot |
		exprAnd |
		exprOr |
		exprImply |
		exprIff |
		exprAX |
		exprEX |
		exprAF |
		exprEF |
		exprAG |
		exprEG |
		exprAU |
		exprEU

	def parse(input: => String): MaybeResult[Ctl] =
		runParser(expr, input)
}
