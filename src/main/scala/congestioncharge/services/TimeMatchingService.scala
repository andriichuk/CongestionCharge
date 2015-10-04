package congestioncharge.services

import congestioncharge.entities._
import congestioncharge.utils.IntervalImplicits._
import congestioncharge.utils.LocalTimeInterval
import org.joda.time._
import com.github.nscala_time.time.Imports._

class TimeMatchingService {

  def getTotalTimeMatchingPolicyRule(trackedTime: Interval, rule: ReceiptPolicyRule): Period = {
    val ruleAllowedDailyIntervals = getRuleAllowedDailyIntervals(trackedTime, rule)
    val ruleAllowedDays = getRuleAllowedDays(trackedTime, rule)
    val finalIntervals = overlapIntervals(ruleAllowedDailyIntervals, ruleAllowedDays)
    sumUpTime(finalIntervals)
  }

  private def getRuleAllowedDailyIntervals(trackedTime: Interval, rule: ReceiptPolicyRule): List[Interval] = {
    def intervalsAccumulator(ruleInterval: Interval, accum: List[Interval]): List[Interval] = {
      if(ruleInterval.getStart().isAfter(trackedTime.getEnd())) accum
      else if(ruleInterval.overlaps(trackedTime)) intervalsAccumulator(ruleInterval.plusDays(1), ruleInterval.overlap(trackedTime) :: accum)
      else intervalsAccumulator(ruleInterval.plusDays(1), accum)
    }
    intervalsAccumulator(rule.timeInterval.toRealTimeInterval(trackedTime.getStart().toLocalDate()), Nil)
  }

  private def getRuleAllowedDays(trackedTime: Interval, rule: ReceiptPolicyRule): List[Interval] = {
    def intervalsAccumulator(date: DateTime, accum: List[Interval]): List[Interval] = {
      if(date.isAfter(trackedTime.getEnd())) {
        accum
      }
      else {
        if(rule.weekDays.contains(date.getDayOfWeek())) intervalsAccumulator(date + 1.days, 24.hours.toIntervalFrom(date) :: accum)
        else intervalsAccumulator(date + 1.days, accum)
      }
    }
    intervalsAccumulator(trackedTime.getStart().withTime(0, 0, 0, 0), Nil)
  }

  private def overlapIntervals(xIntervals: List[Interval], yIntervals: List[Interval]): List[Interval] = {
    (for {
      x <- xIntervals
      y <- yIntervals
    } yield (x, y)).foldLeft(List.empty[Interval]) {
      case (accum, (x, y)) => {
        if(x.overlaps(y)) x.overlap(y) :: accum
        else accum
      }
    }
  }

  private def sumUpTime(intervals: List[Interval]): Period =
    intervals.foldLeft(new Period(0)) { case (p, i) => p.plusMillis(i.toDurationMillis().toInt) }
}
