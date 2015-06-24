package ctlmc.mayberesult

abstract class MaybeResult[+A] {
	// An implicit conversion that converts a MaybeResult to an iterable value
	import scala.language.implicitConversions
	implicit def maybeResult2Iterable[A](xo: MaybeResult[A]): Iterable[A] = xo.toList

	def flatMap[B](f: A => MaybeResult[B]): MaybeResult[B]
	def map[B](f: A => B): MaybeResult[B] = flatMap { a => Result(f(a)) }

	// Work-around for bug: https://issues.scala-lang.org/browse/SI-1336
	// See also: https://stackoverflow.com/questions/4380831/why-does-filter-have-to-be-defined-for-pattern-matching-in-a-for-loop-in-scala
	def withFilter(p: A => Boolean): MaybeResult[A] = this
}

case class Result[+A](val result: A) extends MaybeResult[A] {
	override def flatMap[B](f: A => MaybeResult[B]): MaybeResult[B] = f(result)
}

case class Failure(val message: String) extends MaybeResult[Nothing] {
	override def flatMap[B](f: Nothing => MaybeResult[B]): MaybeResult[B] = Failure(message)
}
