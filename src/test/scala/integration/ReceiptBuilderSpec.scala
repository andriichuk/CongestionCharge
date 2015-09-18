package integration

import congestioncharge.agents.{BuildReceipt, ReceiptBuilder}
import congestioncharge.entities._
import org.joda.time.{DateTime, Interval}
import org.scalatest.{ BeforeAndAfterAll, FlatSpecLike, Matchers }
import akka.actor.{ Actor, Props, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit, TestActorRef }
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.duration._

class ReceiptBuilderSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("ReceiptBuilderSpec"))

  override def afterAll: Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "ReceiptBuilder" should "provide receipt for tracked time" in {
    // TODO
  }
}
