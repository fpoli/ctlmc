package ctlmc.mayberesult

abstract class MaybeResult[T] {
	//def >>=[B](f: T => MaybeResult[B]): MaybeResult[B]
}
case class Result[T](val result: T) extends MaybeResult[T] {
	//def >>=[B](f: T => MaybeResult[B]): MaybeResult[B] = f(result)
}
case class Failure[T](val message: String) extends MaybeResult[T] {
	//def >>=[B](f: T => MaybeResult[B]): Failure[B] = Failure[B](message)
}
