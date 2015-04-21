package calculator

sealed abstract class Expr
final case class Literal(v: Double) extends Expr
final case class Ref(name: String) extends Expr
final case class Plus(a: Expr, b: Expr) extends Expr
final case class Minus(a: Expr, b: Expr) extends Expr
final case class Times(a: Expr, b: Expr) extends Expr
final case class Divide(a: Expr, b: Expr) extends Expr

object Calculator {
  def computeValues(
    namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] = {
 
    println(namedExpressions)
    
    val stuff = Var(24.0)
    val map = Map(("a", Signal(1.0)),
        ("b", Signal(2.0)),
        ("c", Signal(3.0)),
        ("d", Signal(4.0)),
        ("e", Signal(5.0)),
        ("f", Signal(6.0)),
        ("g", Signal(6.0)),
        ("h", Signal(6.0)),
        ("i", Signal(6.0)),
        ("j", Signal(6.0)))
    
    println(map)
    
    map
 
  }

  def eval(expr: Expr, references: Map[String, Signal[Expr]]): Double = {
    ???
  }

  /** Get the Expr for a referenced variables.
   *  If the variable is not known, returns a literal NaN.
   */
  private def getReferenceExpr(name: String,
      references: Map[String, Signal[Expr]]) = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal()
    }
  }
}
