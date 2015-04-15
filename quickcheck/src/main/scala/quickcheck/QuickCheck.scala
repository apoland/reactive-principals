package quickcheck

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._


abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {
  
  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }
  
  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h))==m
  }
  
  //If you insert any two elements into an empty heap, 
  //finding the minimum of the resulting heap should get 
  //the smallest of the two elements back.
  property("min should get smallest") = forAll { (a: Int, b: Int) => 
    val m = if (a < b) a else b 
    val h = insert(b, insert(a, empty))
    findMin(h) == m
  }
  
  //If you insert an element into an empty heap, then delete 
  //the minimum, the resulting heap should be empty.
  property("deleteMin on last element should return empty") = forAll { (a: Int) => 
    val h = insert(a, empty)
    deleteMin(h) == empty
  }

  //Given any heap, you should get a sorted sequence of elements 
  //when continually finding and deleting minima. 
  //(Hint: recursion and helper functions are your friends.)
  property("min deletes should form a sorted sequence") = forAll { (h: H) => 

    def isOrdered(h: H): Boolean = {    
      val m = findMin(h)
      val next = deleteMin(h)
      if (next == empty) true
       else m <= findMin(next) && isOrdered(next)   
    }
   
    if (h == empty) true
     else isOrdered(h) 
     
  }
  
  

  //Finding a minimum of the melding of any two heaps should return 
  //a minimum of one or the other.
  property("min of melded heap should match min of a parent") = forAll { (h1: H, h2: H) => 
    val m = findMin(meld(h1, h2))
    m == findMin(h1) || m == findMin(h2)
  }
  



  lazy val genHeap: Gen[H] =
    for {
      v <- arbitrary[Int]
      m <- oneOf(const(empty), genHeap)
    } yield insert(v, m)

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)
  
  //lazy val genIntList      = Gen.containerOf[List,Int](Gen.oneOf(1, 3, 5, 7))
  
  //implicit lazy val arbList: Arbitrary[List[Int]] = Arbitrary(genIntList)

}

