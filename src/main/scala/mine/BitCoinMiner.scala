package mine
/**
 * @author mebin
 */
import java.security.MessageDigest
import scala.collection.mutable
import scala.util.Random
import scala.collection.mutable.ListBuffer
/*
 *  @author mebin
 */
case object BackendRegistration
final case class JobFailed(reason:String, job: Array[Int])
class BitCoinMiner(noOfZeros:Int) {

  def mine(length:Int): List[String] = {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest();
    val rand = new Random()
    var randInput = ""
    val limit = 10000
    var counter = 0
    var stringAndHashStoringMap = mutable.Map[String, Boolean]()
    var hashValues = new ListBuffer[String]()
    while (counter != limit) {
      randInput = randomString(length)
      if (isBitCoin(randInput, noOfZeros) == true) {
        if(stringAndHashStoringMap.get(randInput).getOrElse(false) == false){
          hashValues += randInput + ' ' + sha(randInput)
          stringAndHashStoringMap.put(randInput, true)
          
        }
      }
	counter += 1  
    }
    hashValues.toList
  }

  private def randomString(length: Int): String = {
    val r = new scala.util.Random
    val sb = new StringBuilder
    for (i <- 1 to length) {
      sb.append(r.nextPrintableChar)
    }
    sb.toString
  }

  private def isBitCoin(str: String, noOfPrefixZeros: Int): Boolean = {
    var count = 0
    val formatted = sha(str)
    toInt(formatted.substring(0, noOfPrefixZeros)).getOrElse(-1) == 0
  }

  private def toInt(in: String): Option[Int] = {
    try {
      Some(Integer.parseInt(in.trim))
    } catch {
      case e: NumberFormatException => None
    }
  }

  private def toHex(bytes: Array[Byte]): String = bytes.map("%02X".format(_)).mkString("")

  private def sha(s: String): String = toHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8")))
}  
