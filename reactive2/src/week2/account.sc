package week2

object account {
 def consolidated(accts: List[BankAccount]): Signal[Int] =
   Signal(accts.map(_.balance()).sum)             //> consolidated: (accts: List[week2.BankAccount])week2.Signal[Int]
 
 val a = new BankAccount()                        //> a  : week2.BankAccount = week2.BankAccount@64616ca2
 val b = new BankAccount()                        //> b  : week2.BankAccount = week2.BankAccount@13fee20c
 val c = consolidated(List(a,b))                  //> c  : week2.Signal[Int] = week2.Signal@26f67b76
 
 c()                                              //> res0: Int = 0
 a deposit 20
 c()                                              //> res1: Int = 20
 b deposit 30
 c()                                              //> res2: Int = 50
 val xchange = Signal(246.00)                     //> xchange  : week2.Signal[Double] = week2.Signal@39fb3ab6
 val inDollar = Signal(c() * xchange())           //> inDollar  : week2.Signal[Double] = week2.Signal@6276ae34
 inDollar()                                       //> res3: Double = 12300.0
 
 
}