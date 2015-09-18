import java.util.concurrent.TimeoutException

import akka.actor._
import akka.actor.ActorSystem._
import akka.pattern.ask
import akka.util.Timeout
import org.joda.time._
import scala.concurrent.Await
import scala.concurrent.duration._
import congestioncharge.agents._
import congestioncharge.entities._

object Main extends App {

  implicit val timeout = Timeout(10 seconds)

  val system = ActorSystem("CongestionChargeSystem")
  implicit val executionContext = system.dispatcher

  val receiptBuilder = system.actorOf(Props[ReceiptBuilder])
  val interval = new Interval(DateTime.now().minusDays(2).getMillis(), DateTime.now().getMillis())
  val future = receiptBuilder ? BuildReceipt(VehicleType.Car, interval)
  val receipt = Await.result(future, timeout.duration).asInstanceOf[Receipt]
  println(s"Receipt received! Total: ${receipt.total}")

  system.shutdown()
}
