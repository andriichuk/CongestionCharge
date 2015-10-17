package congestioncharge.domain.timing

import org.joda.time.DateTimeConstants

object WeekDays {
  val WorkWeek = List(
    DateTimeConstants.MONDAY,
    DateTimeConstants.TUESDAY,
    DateTimeConstants.WEDNESDAY,
    DateTimeConstants.THURSDAY,
    DateTimeConstants.FRIDAY)

  val WeekEnd = List(
    DateTimeConstants.SATURDAY,
    DateTimeConstants.SUNDAY
  )
}
