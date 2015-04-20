package calculator

object TweetLength {
  final val MaxTweetLength = 140
  
  private val remainingChars = Var(MaxTweetLength)
  private val color = Var("green")

  def tweetRemainingCharsCount(tweetText: Signal[String]): Signal[Int] = { 
    remainingChars() = MaxTweetLength - tweetLength(tweetText())
    println("tweetRemainingChars: "+remainingChars())
    remainingChars
  }

  def colorForRemainingCharsCount(remainingCharsCount: Signal[Int]): Signal[String] = {
    val count = remainingCharsCount()
    println("remainingCharsCount: "+count)
    if(remainingCharsCount() > 15) color() = "green"
    if(count <= 14 && count >= 0) color() = "orange"
    if(count < 0) color() = "red"
    println("color: "+color())
    color
  }

  /** Computes the length of a tweet, given its text string.
   *  This is not equivalent to text.length, as tweet lengths count the number
   *  of Unicode *code points* in the string.
   *  Note that this is still a simplified view of the reality. Full details
   *  can be found at
   *  https://dev.twitter.com/overview/api/counting-characters
   */
  private def tweetLength(text: String): Int = {
    /* This should be simply text.codePointCount(0, text.length), but it
     * is not implemented in Scala.js 0.6.2.
     */
    if (text.isEmpty) 0
    else {
      text.length - text.init.zip(text.tail).count(
          (Character.isSurrogatePair _).tupled)
    }
  }
}
