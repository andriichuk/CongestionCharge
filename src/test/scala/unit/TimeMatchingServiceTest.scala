package unit

import congestioncharge.entities._
import congestioncharge.services._
import congestioncharge.utils._
import org.joda.time._
import com.github.nscala_time.time.Imports._
import org.scalatest._

class TimeMatchingServiceTest extends FlatSpec with Matchers with Assertions {

  private val _amRule = ReceiptPolicyRule("AM rate", 2, WeekDays.WorkWeek, LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)))
  private val _pmRule = ReceiptPolicyRule("PM rate", 2.5, WeekDays.WorkWeek, LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
  private val _crossDayRule = ReceiptPolicyRule("Night rate", 0.5, WeekDays.WorkWeek, LocalTimeInterval(new LocalTime(23, 0), new LocalTime(1, 15)))
  private val _carPolicy = ReceiptPolicy(VehicleType.Car, List(_amRule, _pmRule))

  private val _service = new TimeMatchingService()

  "getTotalTimeMatchingPolicyRule" should "correctly match tracked time to a policy rule within one day" in {
    var period = _service.getTotalTimeMatchingPolicyRule(new DateTime(2015, 9, 18, 6, 0) to new DateTime(2015, 9, 18, 9, 0), _amRule)
    assert(period.millis == 2.hours.millis)
    period = _service.getTotalTimeMatchingPolicyRule(new DateTime(2015, 9, 18, 6, 30) to new DateTime(2015, 9, 18, 6, 50), _amRule)
    assert(period.millis == 0)
    period = _service.getTotalTimeMatchingPolicyRule(new DateTime(2015, 9, 18, 7, 20) to new DateTime(2015, 9, 18, 7, 35), _amRule)
    assert(period.millis == 15.minutes.millis)
  }

  "getTotalTimeMatchingPolicyRule" should "sum tracked time across few days" in {
    val period = _service.getTotalTimeMatchingPolicyRule(new DateTime(2015, 9, 17, 10, 0) to new DateTime(2015, 9, 18, 8, 0), _amRule)
    assert(period.millis == 3.hours.millis)  // 2 hours on 17th + 1 hour on 18th
  }

  "getTotalTimeMatchingPolicyRule" should "ignore weekends" in {
    val period = _service.getTotalTimeMatchingPolicyRule(new DateTime(2015, 9, 19, 6, 0) to new DateTime(2015, 9, 19, 9, 0), _amRule)
    assert(period.millis == 0)
  }

  "getTotalTimeMatchingPolicyRule" should "correctly match time for a cross day rule" in {
    val period = _service.getTotalTimeMatchingPolicyRule(new DateTime(2015, 9, 17, 20, 0) to new DateTime(2015, 9, 18, 5, 0), _crossDayRule)
    assert(period.millis == (2.hours + 15.minutes).millis)
  }

}
