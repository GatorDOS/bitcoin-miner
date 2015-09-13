package mine

/**
 * @author mebin
 */

object DistibutedApp {

  def main(args: Array[String]): Unit = {
    // starting 2 frontend nodes and 3 backend nodes
    Boss.main(Seq("2551").toArray)
    Boss.main(Seq("2552").toArray)
//    Worker.main(Seq("4567").toArray)
//    Worker.main(Seq("7890").toArray)
//    Worker.main(Seq("8765").toArray)
  }

}