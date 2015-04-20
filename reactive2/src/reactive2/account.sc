
import reactive2.BankAccount

object account {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val acct = new BankAccount                      //> acct  : reactive2.BankAccount = reactive2.BankAccount@1a407d53
  acct deposit 50
  acct withdraw 20                                //> res0: Int = 30
  acct withdraw 20                                //> res1: Int = 10
  acct withdraw 15                                //> java.lang.Error: insufficient funds
                                                  //| 	at reactive2.BankAccount.withdraw(BankAccount.scala:12)
                                                  //| 	at account$$anonfun$main$1.apply$mcV$sp(account.scala:10)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at account$.main(account.scala:4)
                                                  //| 	at account.main(account.scala)
}