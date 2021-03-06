package kvstore

import akka.actor.{ OneForOneStrategy, Props, ActorRef, Actor }
import kvstore.Arbiter._
import scala.collection.immutable.Queue
import akka.actor.SupervisorStrategy.Restart
import scala.annotation.tailrec
import akka.pattern.{ ask, pipe }
import akka.actor.Terminated
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import akka.actor.PoisonPill
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.util.Timeout


object Replica {
  sealed trait Operation {
    def key: String
    def id: Long
  }
  case class Insert(key: String, value: String, id: Long) extends Operation
  case class Remove(key: String, id: Long) extends Operation
  case class Get(key: String, id: Long) extends Operation

  sealed trait OperationReply
  case class OperationAck(id: Long) extends OperationReply
  case class OperationFailed(id: Long) extends OperationReply
  case class GetResult(key: String, valueOption: Option[String], id: Long) extends OperationReply

  def props(arbiter: ActorRef, persistenceProps: Props): Props = Props(new Replica(arbiter, persistenceProps))
}

class Replica(val arbiter: ActorRef, persistenceProps: Props) extends Actor {
  import Replica._
  import Replicator._
  import Persistence._
  import context.dispatcher

  /*
   * The contents of this actor is just a suggestion, you can implement it in any way you like.
   */
  
  var kv = Map.empty[String, String]
  // a map from secondary replicas to replicators
  var secondaries = Map.empty[ActorRef, ActorRef]
  // the current set of replicators
  var replicators = Set.empty[ActorRef]

  var expectedSeq: Long = 0

  var persistence = context.actorOf(persistenceProps, "persistence")


  def receive = {
    case JoinedPrimary   => context.become(leader)
    case JoinedSecondary => context.become(replica)
  }


  val leader: Receive = {
    case Insert(key, value, id) =>
      kv += (key -> value)
      sender ! OperationAck(id)

    case Remove(key, id) =>
      kv -= key
      sender ! OperationAck(id)
      
    case Get(key, id) =>
      sender ! GetResult(key, kv.get(key), id)

  }

  val replica: Receive = {
    case Snapshot(key, valueOption: Option[String], seq) if seq <= expectedSeq =>
      if (seq == expectedSeq) {
        expectedSeq += 1
        valueOption match {
          case value: Some[String] => kv += (key -> value.get)
          case None => kv -= key
        }
      }

      implicit val timeout = Timeout(100.milliseconds)
      var success = false
      while (!success) {
        val future = persistence ? Persist(key, valueOption, seq)
        try {
          val persisted = Await.result(future, timeout.duration).asInstanceOf[Persisted]
          println(s"SnapshotAck: $persisted")
          sender ! SnapshotAck(persisted.key, persisted.id)
          success = true
        } catch {
          case e: Exception =>
        }

      }


    case Get(key, id) =>
      sender ! GetResult(key, kv.get(key), id)


  }



  //Join the cluster
  arbiter ! Join


}

