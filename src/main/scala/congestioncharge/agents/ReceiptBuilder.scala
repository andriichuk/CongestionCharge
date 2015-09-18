package congestioncharge.agents

import akka.pattern.ask
import akka.actor.{Props, Actor}
import akka.util.Timeout
import congestioncharge.entities._
import congestioncharge.services._
import scala.concurrent.Await
import scala.concurrent.duration._
import VehicleType._
import congestioncharge.entities._
import org.joda.time._

case class BuildReceipt(vehicle: VehicleType, trackedTime: Interval)

class ReceiptBuilder extends Actor {
  import context._

  implicit val timeout = Timeout(10 seconds)

  val receiptBuildingService = new ReceiptBuildingService() // TODO: IoC
  val policyDirectoryActor = context.actorOf(Props[PolicyRepository])

  def receive = {
    case BuildReceipt(vehicle, trackedTime) => {

      val policy = Await.result(policyDirectoryActor ? RequestPolicy(vehicle), timeout.duration).asInstanceOf[ReceiptPolicy]
      val receipt = receiptBuildingService.buildReceipt(policy, trackedTime)
      sender() ! receipt
    }
  }
}
