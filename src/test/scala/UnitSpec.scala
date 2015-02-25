package ctlmc.spec

import org.scalatest._

abstract class UnitSpec extends FunSuite with Matchers with
	OptionValues with Inside with Inspectors
