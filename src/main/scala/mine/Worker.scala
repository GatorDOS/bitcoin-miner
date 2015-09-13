package mine

import com.typesafe.config.ConfigFactory

import akka.actor.Actor
import akka.actor.ActorSelection.toScala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.RootActorPath
import akka.actor.actorRef2Scala
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.Member
import akka.cluster.MemberStatus

class Worker extends Actor{
  
  val cluster = Cluster(context.system)
  
  //subscribe to cluster changes, MemberUp
  //re-subscribe when restart
  
  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)
  
  def receive = {
    case noOfZeros:Array[Int] => sender() ! new BitCoinMiner(noOfZeros(0)).mine(noOfZeros(1))
    case state: CurrentClusterState => 
       state.members.filter(_.status == MemberStatus.Up) foreach register
    case MemberUp(m) => register(m)   
  }
  
  def register(member: Member): Unit = 
    if(member.hasRole("Boss"))
      context.actorSelection(RootActorPath(member.address) / "user" / "Boss") ! BackendRegistration
  
}

object Worker {
  def main(args: Array[String]): Unit = {
    //Override the configuration  of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    withFallback(ConfigFactory.parseString("akka.cluster.roles = [worker]")).
    withFallback(ConfigFactory.load())
    
    val system = ActorSystem("ClusterSystem", config)
    system.actorOf(Props[Worker], name = "worker")
  }
}