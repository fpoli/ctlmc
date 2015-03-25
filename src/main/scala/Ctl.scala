package ctlmc.ctl

abstract class Ctl

case object True extends Ctl
case object False extends Ctl
case class Atom(val name: String, val value: String) extends Ctl {
	override def toString() = "\"" + name + "\"=\"" + value + "\""
}
case class Not(val x: Ctl) extends Ctl
case class And(val x: Ctl, val y: Ctl) extends Ctl
case class Or(val x: Ctl, val y: Ctl) extends Ctl
case class Imply(val x: Ctl, val y: Ctl) extends Ctl
case class Iff(val x: Ctl, val y: Ctl) extends Ctl

case class AX(val x: Ctl) extends Ctl
case class EX(val x: Ctl) extends Ctl
case class AF(val x: Ctl) extends Ctl
case class EF(val x: Ctl) extends Ctl
case class AG(val x: Ctl) extends Ctl
case class EG(val x: Ctl) extends Ctl
case class AU(val x: Ctl, val y: Ctl) extends Ctl
case class EU(val x: Ctl, val y: Ctl) extends Ctl
