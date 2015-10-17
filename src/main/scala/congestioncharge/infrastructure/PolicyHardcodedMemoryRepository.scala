package congestioncharge.infrastructure

import congestioncharge.domain.charging._
import congestioncharge.domain.tracking.VehicleType
import VehicleType._
import congestioncharge.domain.timing.{LocalTimeInterval, WeekDays}
import org.joda.time.LocalTime

class PolicyHardcodedMemoryRepository extends PolicyRepository {

  private val _carPolicy = ChargePolicy(VehicleType.Car, List(
    ChargePolicyRule("AM rate", 2, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0))),
    ChargePolicyRule("PM rate", 2.5, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
  )
  )
  private val _motorbikePolicy = ChargePolicy(VehicleType.Motorbike, List(
    ChargePolicyRule("AM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0))),
    ChargePolicyRule("PM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
  )
  )
  private val _policies = Map(VehicleType.Car -> _carPolicy, VehicleType.Motorbike -> _motorbikePolicy)

  def getPolicy(vehicleType: VehicleType) = _policies(vehicleType)
}
