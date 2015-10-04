package congestioncharge.utils

import org.joda.time._

object IntervalImplicits {

  implicit class RichInterval(interval: Interval) {

    def plusDays(i: Int) = interval.withEnd(interval.getEnd().plusDays(i)).withStart(interval.getStart().plusDays(i))

  }

}



