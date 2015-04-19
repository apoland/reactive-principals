package week2

object week2 {
   object sim extends Circuits with Parameters
   import sim._
   
   val in1, in2, sum, carry = new Wire            //> in1  : week2.week2.sim.Wire = week2.Gates$Wire@7d6f77cc
                                                  //| in2  : week2.week2.sim.Wire = week2.Gates$Wire@3d8c7aca
                                                  //| sum  : week2.week2.sim.Wire = week2.Gates$Wire@5ebec15
                                                  //| carry  : week2.week2.sim.Wire = week2.Gates$Wire@21bcffb5
   halfAdder(in1, in2, sum, carry)
   probe("sum", sum)                              //> sum 0 new-value = false
   probe("carry", carry)                          //> carry 0 new-value = false
 
   in1 setSignal true
   run()                                          //> *** simulation started, time = 0 ***
                                                  //| sum 5 new-value = true
                                                  //| sum 10 new-value = false
                                                  //| sum 10 new-value = true
 
 
   in2 setSignal true
   run()                                          //> *** simulation started, time = 10 ***
                                                  //| carry 13 new-value = true
                                                  //| sum 18 new-value = false
   
   in1 setSignal false
   run()                                          //> *** simulation started, time = 18 ***
                                                  //| carry 21 new-value = false
                                                  //| sum 26 new-value = true
}