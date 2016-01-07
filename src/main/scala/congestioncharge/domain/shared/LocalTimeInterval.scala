package congestioncharge.domain.shared

import org.joda.time._

class LocalTimeInterval(val from: LocalTime, val to: LocalTime) {

  def toRealTimeInterval(refDate: LocalDate) = {
    var realFrom = refDate.toDateTime(from)
    val realTo = refDate.toDateTime(to)
    if(from.isAfter(to))
      realFrom = realFrom.minusDays(1)
    new Interval(realFrom.getMillis(), realTo.getMillis())
  }

}