package congestioncharge.agents

import akka.actor.Actor
import congestioncharge.entities._
import VehicleType._
import congestioncharge.entities._
import congestioncharge.utils._
import org.joda.time._

case class RequestPolicy(vehicle: VehicleType)

class PolicyRepository extends Actor {

  // TODO: policies should be loaded from external repo and caching implemented
  private val _carPolicy = ReceiptPolicy(VehicleType.Car, List(
    ReceiptPolicyRule("AM rate", 2, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0))),
    ReceiptPolicyRule("PM rate", 2.5, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
    )
  )
  private val _motorbikePolicy = ReceiptPolicy(VehicleType.Motorbike, List(
    ReceiptPolicyRule("AM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0))),
    ReceiptPolicyRule("PM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
    )
  )
  private val _policies = Map(VehicleType.Car -> _carPolicy, VehicleType.Motorbike -> _motorbikePolicy)

  def receive = {
    case RequestPolicy(vehicleType) => {
      sender() ! _policies(vehicleType)
    }
  }
}
