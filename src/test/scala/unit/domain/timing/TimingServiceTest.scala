package unit.domain.timing

import com.github.nscala_time.time.Imports._
import congestioncharge.domain.charging.{ChargePolicy, ChargePolicyRule}
import congestioncharge.domain.timing.{LocalTimeInterval, TimingService, WeekDays}
import congestioncharge.domain.tracking.VehicleType
import org.scalatest._

class TimingServiceTest extends FlatSpec with Matchers with Assertions {

  private val _service = new TimingService()

  "getTotalTimeMatchingPolicyRule" should "correctly match time intervals within one day" in {
    val intervals = _service.matchTimeIntervalToDailyIntervals(
      new DateTime(2015, 9, 18, 6, 0) to new DateTime(2015, 9, 18, 9, 0),
      new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)),
      WeekDays.WorkWeek)
    assert(intervals.length == 1)
    assert(intervals(0).startMillis == new DateTime(2015, 9, 18, 7, 0).getMillis())
    assert(intervals(0).endMillis == new DateTime(2015, 9, 18, 9, 0).getMillis())
  }

  "getTotalTimeMatchingPolicyRule" should "return empty if intervals don't match" in {
    val intervals = _service.matchTimeIntervalToDailyIntervals(
      new DateTime(2015, 9, 18, 6, 30) to new DateTime(2015, 9, 18, 6, 50),
      new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)),
      WeekDays.WorkWeek)
    assert(intervals.length == 0)
  }

  "getTotalTimeMatchingPolicyRule" should "return several intervals if time spans few days" in {
    val intervals = _service.matchTimeIntervalToDailyIntervals(
      new DateTime(2015, 9, 17, 10, 0) to new DateTime(2015, 9, 18, 8, 0),
      new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)),
      WeekDays.WorkWeek)
    assert(intervals.length == 2)
    assert(intervals(0).startMillis == new DateTime(2015, 9, 17, 10, 0).getMillis())
    assert(intervals(0).endMillis == new DateTime(2015, 9, 17, 12, 0).getMillis())
    assert(intervals(1).startMillis == new DateTime(2015, 9, 18, 7, 0).getMillis())
    assert(intervals(1).endMillis == new DateTime(2015, 9, 18, 8, 0).getMillis())
  }

  "getTotalTimeMatchingPolicyRule" should "ignore weekends" in {
    val intervals = _service.matchTimeIntervalToDailyIntervals(
      new DateTime(2015, 9, 19, 6, 0) to new DateTime(2015, 9, 19, 9, 0),
      new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)),
      WeekDays.WorkWeek)
    assert(intervals.length == 0)
  }

  "getTotalTimeMatchingPolicyRule" should "correctly match time intervals for a cross day rule" in {
    val intervals = _service.matchTimeIntervalToDailyIntervals(
      new DateTime(2015, 9, 17, 20, 0) to new DateTime(2015, 9, 18, 5, 0),
      new LocalTimeInterval(new LocalTime(23, 0), new LocalTime(1, 15)),
      WeekDays.WorkWeek)
    assert(intervals.length == 2)
    assert(intervals(0).startMillis == new DateTime(2015, 9, 17, 23, 0).getMillis())
    assert(intervals(0).endMillis == new DateTime(2015, 9, 18, 0, 0).getMillis())
    assert(intervals(1).startMillis == new DateTime(2015, 9, 18, 0, 0).getMillis())
    assert(intervals(1).endMillis == new DateTime(2015, 9, 18, 1, 15).getMillis())
  }

  "sumUpIntervals" should "correctly sum up intervals" in {
    val period = _service.sumUpIntervals(List(
      new DateTime(2015, 9, 19, 6, 0) to new DateTime(2015, 9, 19, 9, 15),
      new DateTime(2015, 9, 19, 15, 0) to new DateTime(2015, 9, 19, 16, 0)
    ))

    assert(period.millis == (4.hours + 15.minutes).millis)
  }

}
