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
/**
 * @author mebin
 */
class Boss extends Actor {
  var backends = IndexedSeq.empty[ActorRef]
  var jobCounter = 0

  def receive = {
    case job: Int if backends.isEmpty =>
      sender() ! JobFailed("Service UnAvailable, try again later", job)
    case job: Int =>
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
  def main(args: Array[String]): Unit = {
    //Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [Boss]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
    val boss = system.actorOf(Props[Boss], name = "Boss")
//    println(" Please Enter the value of k")
    val input = 4
    val counter = new AtomicInteger(9)
    
    import system.dispatcher
    system.scheduler.schedule(2.seconds, 30.seconds) {
      implicit val timeout = Timeout(5 seconds)
      (boss ? input) onSuccess {//counter.incrementAndGet()
        case result:List[Any] => result.foreach { x => println("mebinjacob;" + x) }
        case result1 => println(result1)
      }
    }
      
  }

}