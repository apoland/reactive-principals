package quickcheck

object Test {
  val ibh = new IntHeap with BinomialHeap         //> ibh  : quickcheck.IntHeap with quickcheck.BinomialHeap = quickcheck.Test$$ano
                                                  //| nfun$main$1$$anon$1@12bb4df8
  
  val myIntHeap = ibh.empty                       //> myIntHeap  : scala.collection.immutable.Nil.type = List()
  val myIntHeap2 = ibh.insert(3, myIntHeap)       //> myIntHeap2  : quickcheck.Test.ibh.H = List(Node(3,0,List()))
}