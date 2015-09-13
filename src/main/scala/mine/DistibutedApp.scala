package mine

import scala.io.StdIn

/**
 * @author mebin
 */

object DistibutedApp {

  def main(args: Array[String]): Unit = {
    // starting 2 frontend nodes 
    println("Please enter the value of k  : ")
    val input:Int = StdIn.readInt()
    Boss.start(Seq("2551").toArray, input)
    Boss.start(Seq("2552").toArray, input)
//    Worker.main(Seq("4567").toArray)
//    Worker.main(Seq("7890").toArray)
//    Worker.main(Seq("8765").toArray)
  }

}