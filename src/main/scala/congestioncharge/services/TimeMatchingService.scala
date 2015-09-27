package congestioncharge.services

import congestioncharge.entities._
import congestioncharge.utils.IntervalImplicits._
import congestioncharge.utils.LocalTimeInterval
import org.joda.time._

class TimeMatchingService {

  def getTotalTimeMatchingPolicyRule(trackedTime: Interval, rule: ReceiptPolicyRule): Period = {
    (for {
      i <- trackedTime.splitByDays().filter { i => rule.weekDays.contains(i.getStart().getDayOfWeek()) }
      r <- splitIfCrossDay(rule)
    } yield (i, r))
      .foldLeft (Period.ZERO) { case (p, (i, r)) => {
        val ruleStart = i.getStart().toLocalDate().toDateTime(r.timeInterval.from)
        var ruleEnd = i.getStart().toLocalDate().toDateTime(r.timeInterval.to)
        if(r.timeInterval.to == LocalTime.MIDNIGHT)
          ruleEnd = ruleEnd.plusDays(1)

        val ruleInterval = new Interval(ruleStart, ruleEnd)
        if (ruleInterval.overlaps(i))
          p.plusMillis(ruleInterval.overlap(i).toDurationMillis().toInt)
        else
          p
    }}
  }

  private def splitIfCrossDay(rule: ReceiptPolicyRule) = {
    if(rule.timeInterval.from.getMillisOfDay() <= rule.timeInterval.to.getMillisOfDay())
      List(rule)
    else
      List(
        ReceiptPolicyRule(rule.name, rule.pricePerHour, rule.weekDays, LocalTimeInterval(LocalTime.MIDNIGHT, rule.timeInterval.to)),
        ReceiptPolicyRule(rule.name, rule.pricePerHour, rule.weekDays, LocalTimeInterval(rule.timeInterval.from, LocalTime.MIDNIGHT)))
  }

}
