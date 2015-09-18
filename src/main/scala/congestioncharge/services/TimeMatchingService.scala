package congestioncharge.services

import congestioncharge.entities._
import congestioncharge.utils.IntervalImplicits._
import org.joda.time._

class TimeMatchingService {

  def getTotalTimeMatchingPolicyRule(trackedTime: Interval, rule: ReceiptPolicyRule): Period = {
    trackedTime
      .splitByDays()
      .filter { i => rule.weekDays.contains(i.getStart().getDayOfWeek()) }
      .foldLeft (Period.ZERO) { (p, i) => {
        val ruleStart = i.getStart().toLocalDate().toDateTime(rule.timeInterval.from)
        val ruleEnd = i.getStart().toLocalDate().toDateTime(rule.timeInterval.to)
        val ruleInterval = new Interval(ruleStart, ruleEnd)
        if (ruleInterval.overlaps(i))
          p.plusMillis(ruleInterval.overlap(i).toDurationMillis().toInt)
        else
          p
    }}
  }

}
