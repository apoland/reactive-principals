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

  }


}