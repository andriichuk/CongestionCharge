package unit.domain.timing

import congestioncharge.domain.timing.LocalTimeInterval
import org.joda.time._
import org.scalatest._

class LocalTimeIntervalTest extends FlatSpec with Matchers with Assertions {
  "toRealTimeInterval" should "apply refDate to time interval" in {
    val interval = new LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0))
    val refDate = new LocalDate(2015, 10, 5)
    val result = interval.toRealTimeInterval(refDate)

    assert(result.getStart() === new DateTime(2015, 10, 5, 7, 0))
    assert(result.getEnd() === new DateTime(2015, 10, 5, 12, 0))
  }
}
