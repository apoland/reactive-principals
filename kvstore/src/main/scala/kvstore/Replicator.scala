package kvstore

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.{ ask, pipe }
import kvstore.Persistence.{Persisted, Persist}
import scala.concurrent.Await
import scala.concurrent.duration._

object Replicator {
  case class Replicate(key: String, valueOption: Option[String], id: Long)
  case class Replicated(key: String, id: Long)
  
  case class Snapshot(key: String, valueOption: Option[String], seq: Long)
  case class SnapshotAck(key: String, seq: Long)

  def props(replica: ActorRef): Props = Props(new Replicator(replica))
}

class Replicator(val replica: ActorRef) extends Actor {
  import Replicator._
  import Replica._
  import context.dispatcher
  
  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */

  // map from sequence number to pair of sender and request
  var acks = Map.empty[Long, (ActorRef, Replicate)]
  // a sequence of not-yet-sent snapshots (you can disregard this if not implementing batching)
  var pending = Vector.empty[Snapshot]
  
  var _seqCounter = 0L
  def nextSeq = {
    val ret = _seqCounter
    _seqCounter += 1
    ret
  }

  def receive: Receive = {
    case Replicate(key, valueOption, id) =>
    // secondary should continue to send until replica replies: SnapshotAck("k1", 0L)
    implicit val timeout = Timeout(100.milliseconds)
      var success = false
      while (!success) {
        val future = replica ? Snapshot(key, valueOption, id)
        try {
           Await.result(future, timeout.duration).asInstanceOf[SnapshotAck]
           success = true
        } catch {
          case e: Exception =>
        }

      }
  }

}
