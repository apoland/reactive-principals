package andrewtest

import rx.lang.scala.Observable
import scala.concurrent.duration._
import scala.language.postfixOps

object Main {

  //Hello world for Observable!

  def main(args: Array[String]) {
    val bufs = Observable.interval(1 seconds).filter(_ % 2 == 0).slidingBuffer(count=3,skip=1)
    val s = bufs.subscribe(println(_))

    readLine()

    s.unsubscribe()

    val xs: Observable[Int] = Observable.from(List(3,2,1))
    val yss: Observable[Observable[Int]] =
      xs.map(x => Observable.interval(x seconds).map(_=>x).take(2))
    val zs: Observable[Int] = yss.flatten

    val t = zs.subscribe(println(_))

    readLine()

    t.unsubscribe()

  }


}