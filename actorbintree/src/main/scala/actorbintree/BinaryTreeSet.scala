/**
 * Copyright (C) 2009-2013 Typesafe Inc. <http://www.typesafe.com>
 */
package actorbintree

import akka.actor._
import scala.collection.immutable.Queue
import scala.util.{Failure, Success}

object BinaryTreeSet {

  trait Operation {
    def requester: ActorRef
    def id: Int
    def elem: Int
  }

  trait OperationReply {
    def id: Int
  }

  /** Request with identifier `id` to insert an element `elem` into the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Insert(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to check whether an element `elem` is present
    * in the tree. The actor at reference `requester` should be notified when
    * this operation is completed.
    */
  case class Contains(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request with identifier `id` to remove the element `elem` from the tree.
    * The actor at reference `requester` should be notified when this operation
    * is completed.
    */
  case class Remove(requester: ActorRef, id: Int, elem: Int) extends Operation

  /** Request to perform garbage collection*/
  case object GC

  /** Holds the answer to the Contains request with identifier `id`.
    * `result` is true if and only if the element is present in the tree.
    */
  case class ContainsResult(id: Int, result: Boolean) extends OperationReply
  
  /** Message to signal successful completion of an insert or remove operation. */
  case class OperationFinished(id: Int) extends OperationReply

}


class BinaryTreeSet extends Actor {
  import BinaryTreeSet._
  import BinaryTreeNode._

  def createRoot: ActorRef = context.actorOf(BinaryTreeNode.props(0, initiallyRemoved = true))

  var root = createRoot

  // optional
  var pendingQueue = Queue.empty[Operation]

  // optional
  def receive = normal

  // optional
  /** Accepts `Operation` and `GC` messages. */
  val normal: Receive = {
    case msg: Operation => root ! msg
    case GC => {
      //println("****GC****")
      // ignore messages while GCing
      // build a new root node
      // send QueryValue message to root node
      // use Value replies to populate new root node
      // swap new node to be new root
    }
  }

  // optional
  /** Handles messages while garbage collection is performed.
    * `newRoot` is the root of the new binary tree where we want to copy
    * all non-removed elements into.
    */
  def garbageCollecting(newRoot: ActorRef): Receive = {
    case _ => println("garbageCollecting")
  }

}

object BinaryTreeNode {
  trait Position

  case object Left extends Position
  case object Right extends Position

  case class CopyTo(treeNode: ActorRef)
  case object CopyFinished

  def props(elem: Int, initiallyRemoved: Boolean) = Props(classOf[BinaryTreeNode],  elem, initiallyRemoved)
}

class BinaryTreeNode(val elem: Int, initiallyRemoved: Boolean) extends Actor {
  import BinaryTreeNode._
  import BinaryTreeSet._

  var subtrees = Map[Position, ActorRef]()
  var removed = initiallyRemoved

  // optional
  def receive = normal

  // optional
  /** Handles `Operation` messages and `CopyTo` requests. */
  val normal: Receive = {
    case msg: Insert => {
      if (msg.elem == elem) {
        removed = false
        msg.requester ! OperationFinished(msg.id)
      }
      if (msg.elem < elem) insert(Left, msg)
      if (msg.elem > elem) insert(Right, msg)
    }
    case msg: Contains => {
      if (msg.elem == elem) msg.requester ! ContainsResult(msg.id, !removed)
      if (msg.elem < elem) contains(Left, msg)
      if (msg.elem > elem) contains(Right, msg)
    }
    case msg: Remove => {
      if (msg.elem == elem) {
        removed = true
        msg.requester ! OperationFinished(msg.id)
      }
      if (msg.elem < elem) remove(Left, msg)
      if (msg.elem > elem) remove(Right, msg)
    }
  }

  // optional
  /** `expected` is the set of ActorRefs whose replies we are waiting for,
    * `insertConfirmed` tracks whether the copy of this node to the new tree has been confirmed.
    */
  def copying(expected: Set[ActorRef], insertConfirmed: Boolean): Receive = {
    case _ => println("copying")
  }

  def insert(position: Position, msg: Insert): Unit = {
    subtrees.get(position) match {
      case Some(a) => a ! msg
      case None => {
        subtrees += (position -> context.actorOf(BinaryTreeNode.props(msg.elem, initiallyRemoved = false)))
        msg.requester ! OperationFinished(msg.id)
      }
    }
  }

  def contains(position: Position, msg: Contains): Unit = {
    subtrees.get(position) match {
      case Some(a) => a ! msg
      case None =>  msg.requester ! ContainsResult(msg.id, false)
    }
  }

  def remove(position: Position, msg: Remove): Unit = {
    subtrees.get(position) match {
      case Some(a) => a ! msg
      case None =>  msg.requester ! OperationFinished(msg.id)
    }
  }


}
