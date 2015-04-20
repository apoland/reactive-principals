package week2

class BankAccount {
  val balance = Var(0)
  def deposit(amount: Int): Unit = {
    val b = balance()
    if (amount > 0) balance() = b + amount
  }

  def withdraw(amount: Int): Int = {
    val b = balance()
    if (0 < amount && amount <= b) {
      
      balance() = b - amount;
      balance()
    } else throw new Error("insufficient funds");
  }
}
