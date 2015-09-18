package congestioncharge.utils

import org.joda.time._

object IntervalImplicits {

  implicit class RichInterval(i: Interval) {
    def splitByDays() = getDailyIntervals(i.getStartMillis(), i.getEndMillis())

    private def getDailyIntervals(startDayMillis: Long, endMillis: Long): List[Interval] = {
      if (startDayMillis == endMillis)
        Nil
      else {
        val endDayMillis = Math.min(new DateTime(startDayMillis).plusDays(1).withTimeAtStartOfDay().getMillis(), endMillis)
        new Interval(startDayMillis, endDayMillis) :: getDailyIntervals(endDayMillis, endMillis)
      }
    }
  }

}



