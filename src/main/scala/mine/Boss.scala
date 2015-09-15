package mine

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.DurationInt
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import java.io.File
import java.io.PrintWriter
import java.io.FileWriter
import akka.actor.DeadLetter
/**
 * @author mebin
 */
class Boss extends Actor {
  var backends = IndexedSeq.empty[ActorRef]
  var jobCounter = 0

  def receive = {
    case d: DeadLetter => println(d)
    case job: Array[Int] if backends.isEmpty =>
      sender() ! JobFailed("Service UnAvailable, try again later", job)
    case job: Array[Int] =>
      jobCounter += 1
      backends(jobCounter % backends.size) forward job

    case BackendRegistration if !backends.contains(sender()) =>
      context watch sender()
      backends = backends :+ sender()

    case Terminated(a) =>
      backends = backends.filterNot(_ == a)
  }
}

object Boss {
  def start(args: Array[String], noOfZerosPrefix: Int): Unit = {
    //Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [Boss]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
    val boss = system.actorOf(Props[Boss], name = "Boss")
    system.eventStream.subscribe(boss, classOf[DeadLetter])
    var length = new AtomicInteger(9)
    println("The number if zeros prefix required are " + noOfZerosPrefix)
    val f = new File("BitCoin.txt");
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 240.seconds) {
      implicit val timeout = Timeout(60 seconds)
      val future = boss ? Array(noOfZerosPrefix, length.incrementAndGet())
      future onSuccess {
        
        case result: Result  => val writer = new PrintWriter(new FileWriter(f,true)) 
         println(result)
        /*result.foreach {
          x => 
          writer.write("mebinjacob;" + x + "\n") println("mebinjacob;" + x)
        }
        writer.close*/
        case result1 => println(result1)
      }
    }

  }

}