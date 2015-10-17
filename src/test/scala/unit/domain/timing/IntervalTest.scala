package unit.domain.timing

import congestioncharge.domain.timing.IntervalImplicits
import congestioncharge.domain.timing.IntervalImplicits._
import org.joda.time._
import org.scalatest._

class IntervalTest extends FlatSpec with Matchers with Assertions {
  "plusDays" should "add n days to interval" in {
    val interval = new Interval(new DateTime(2015, 10, 5, 7, 0), new DateTime(2015, 10, 6, 12, 0))
    val result = interval.plusDays(3)

    assert(result.getStart() === new DateTime(2015, 10, 8, 7, 0))
    assert(result.getEnd() === new DateTime(2015, 10, 9, 12, 0))
  }
}
