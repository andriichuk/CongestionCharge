package unit.domain.charging

import congestioncharge.domain.charging._
import congestioncharge.domain.core.{VehicleType, Vehicle}
import congestioncharge.domain.timing._
import congestioncharge.domain.tracking._
import congestioncharge.infrastructure.PolicyHardcodedMemoryRepository
import org.joda.time.DateTime
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest._
import com.github.nscala_time.time.Imports._
import org.scalatest.mock.MockitoSugar

class ChargeServiceTest extends FlatSpec with Matchers with Assertions with MockitoSugar {

  private val _timingService = new TimingService
  private val _policyRepository = mock[PolicyRepository]
  when(_policyRepository.getPolicy(VehicleType.Car)).thenReturn(ChargePolicy(VehicleType.Car, List(
    ChargePolicyRule("AM rate", 2, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0))),
    ChargePolicyRule("PM rate", 2.5, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
  )))

  when(_policyRepository.getPolicy(VehicleType.Motorbike)).thenReturn(ChargePolicy(VehicleType.Motorbike, List(
    ChargePolicyRule("AM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0))),
    ChargePolicyRule("PM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
  )))
  private val _service = new ChargeService(_timingService, _policyRepository)
  private val _car = new Vehicle("1", VehicleType.Car)
  private val _motorbike = new Vehicle("2", VehicleType.Motorbike)

  "charge" should "provide correct receipt for: 'Car: 24/04/2008 11:32 - 24/04/2008 14:42'" in {
    val receipt = _service.charge(_car, new DateTime(2008, 4, 24, 11, 32) to new DateTime(2008, 4, 24, 14, 42))
    assert(receipt.lines.length == 2)
    assert(receipt.lines(0).period.toStandardDuration().getMillis() == 28.minutes.millis)
    assert(receipt.lines(0).total == 0.9)
    assert(receipt.lines(1).period.toStandardDuration().getMillis() == (2.hours + 42.minutes).millis)
    assert(receipt.lines(1).total == 6.7)
    assert(receipt.total == 7.6)
  }

  "charge" should "provide correct receipt for: 'Motorbike: 24/04/2008 17:00 - 24/04/2008 22:11'" in {
    val receipt = _service.charge(_motorbike, new DateTime(2008, 4, 24, 17, 0) to new DateTime(2008, 4, 24, 22, 11))
    assert(receipt.lines.length == 2)
    assert(receipt.lines(0).period.toStandardDuration().getMillis() == 0)
    assert(receipt.lines(0).total == 0)
    assert(receipt.lines(1).period.toStandardDuration().getMillis() == 2.hours.millis)
    assert(receipt.lines(1).total == 2)
    assert(receipt.total == 2)
  }

  "charge" should "provide correct receipt for: 'Van: 25/04/2008 10:23 - 28/04/2008 09:02'" in {
    val receipt = _service.charge(_car, new DateTime(2008, 4, 25, 10, 23) to new DateTime(2008, 4, 28, 9, 2))
    assert(receipt.lines.length == 2)
    assert(receipt.lines(0).period.toStandardDuration().getMillis() == (3.hours + 39.minutes).millis)
    assert(receipt.lines(0).total == 7.3)
    assert(receipt.lines(1).period.toStandardDuration().getMillis() == 7.hours.millis)
    assert(receipt.lines(1).total == 17.5)
    assert(receipt.total == 24.8)
  }
}
