package unit

import congestioncharge.entities.{ReceiptPolicyRule, VehicleType, ReceiptPolicy}
import congestioncharge.services.ReceiptBuildingService
import congestioncharge.utils._
import org.joda.time._
import com.github.nscala_time.time.Imports._
import org.scalatest._

class ReceiptBuildingServiceTest extends FlatSpec with Matchers with Assertions {

  private val _carAmRule = ReceiptPolicyRule("AM rate", 2, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)))
  private val _carPmRule = ReceiptPolicyRule("PM rate", 2.5, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
  private val _carPolicy = ReceiptPolicy(VehicleType.Car, List(_carAmRule, _carPmRule))

  private val _motorbikeAmRule = ReceiptPolicyRule("AM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)))
  private val _motorbikePmRule = ReceiptPolicyRule("PM rate", 1, WeekDays.WorkWeek, new LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
  private val _motorbikePolicy = ReceiptPolicy(VehicleType.Car, List(_motorbikeAmRule, _motorbikePmRule))

  private val _service = new ReceiptBuildingService()

  "buildReceipt" should "provide correct receipt for: 'Car: 24/04/2008 11:32 - 24/04/2008 14:42'" in {
    val receipt = _service.buildReceipt(_carPolicy, new DateTime(2008, 4, 24, 11, 32) to new DateTime(2008, 4, 24, 14, 42))
    assert(receipt.lines.length == 2)
    assert(receipt.lines(0).period.toStandardDuration().getMillis() == 28.minutes.millis)
    assert(receipt.lines(0).total == 0.9)
    assert(receipt.lines(1).period.toStandardDuration().getMillis() == (2.hours + 42.minutes).millis)
    assert(receipt.lines(1).total == 6.7)
    assert(receipt.total == 7.6)
  }

  "buildReceipt" should "provide correct receipt for: 'Motorbike: 24/04/2008 17:00 - 24/04/2008 22:11'" in {
    val receipt = _service.buildReceipt(_motorbikePolicy, new DateTime(2008, 4, 24, 17, 0) to new DateTime(2008, 4, 24, 22, 11))
    assert(receipt.lines.length == 2)
    assert(receipt.lines(0).period.toStandardDuration().getMillis() == 0)
    assert(receipt.lines(0).total == 0)
    assert(receipt.lines(1).period.toStandardDuration().getMillis() == 2.hours.millis)
    assert(receipt.lines(1).total == 2)
    assert(receipt.total == 2)
  }

  "buildReceipt" should "provide correct receipt for: 'Van: 25/04/2008 10:23 - 28/04/2008 09:02'" in {
    val receipt = _service.buildReceipt(_carPolicy, new DateTime(2008, 4, 25, 10, 23) to new DateTime(2008, 4, 28, 9, 2))
    assert(receipt.lines.length == 2)
    assert(receipt.lines(0).period.toStandardDuration().getMillis() == (3.hours + 39.minutes).millis)
    assert(receipt.lines(0).total == 7.3)
    assert(receipt.lines(1).period.toStandardDuration().getMillis() == 7.hours.millis)
    assert(receipt.lines(1).total == 17.5)
    assert(receipt.total == 24.8)
  }

  "buildReceiptLine" should "return filled receipt line" in {
    val line = _service.buildReceiptLine(_carAmRule, new DateTime(2015, 9, 18, 6, 0) to new DateTime(2015, 9, 18, 9, 0))
    assert(line != null)
    assert(line.period.millis != 0 )
    assert(line.title != "")
    assert(line.total != 0)
  }

}
