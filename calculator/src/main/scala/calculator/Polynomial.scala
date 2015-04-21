package calculator

object Polynomial {
  
  def computeDelta(a: Signal[Double], b: Signal[Double],
      c: Signal[Double]): Signal[Double] = {
    Signal((b() * b()) - (4* a() * c()))
  }

  def computeSolutions(a: Signal[Double], b: Signal[Double],
      c: Signal[Double], delta: Signal[Double]): Signal[Set[Double]] = {
       
    val sqrtDelta = math.sqrt(delta())
    val sol1 = ( -b() + sqrtDelta ) / ( 2 * a() )
    val sol2 = ( -b() - sqrtDelta ) / ( 2 * a() )
    
    if (delta() < 0) Signal(Set())
    else Signal(Set(sol1, sol2))
    
  }
}
