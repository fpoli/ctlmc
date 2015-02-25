package ctlmc

object fixpoint {
	def apply[T](
		f: T => T,
		equals: (T, T) => Boolean = (a: T, b: T) => (a == b)
	)(startValue: T) : T = {
		def iterate(value: T): T = {
			val newValue = f(value)
			if (equals(value, newValue)) newValue
			else iterate(newValue)
		}
		iterate(startValue)
	}
}
