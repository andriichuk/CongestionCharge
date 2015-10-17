package congestioncharge.domain.timing

import com.github.nscala_time.time.Imports._
import IntervalImplicits._

class TimingService {

  def matchTimeIntervalToDailyIntervals(timeToMatch: Interval, dailyInterval: LocalTimeInterval, weekDays: List[Int]): List[Interval] = {
    val dailyIntervals = getRuleAllowedDailyIntervals(timeToMatch, dailyInterval)
    val wholeDayIntervals = getRuleAllowedDays(timeToMatch, dailyInterval, weekDays)
    overlapIntervals(dailyIntervals, wholeDayIntervals)
  }

  def sumUpIntervals(intervals: List[Interval]): Period =
    intervals.foldLeft(new Period(0)) { case (p, i) => p.plusMillis(i.toDurationMillis().toInt) }

  private def getRuleAllowedDailyIntervals(timeToMatch: Interval, dailyInterval: LocalTimeInterval): List[Interval] = {
    def intervalsAccumulator(timeInterval: Interval, accum: List[Interval]): List[Interval] = {
      if(timeInterval.getStart().isAfter(timeToMatch.getEnd())) accum
      else if(timeInterval.overlaps(timeToMatch)) intervalsAccumulator(timeInterval.plusDays(1), timeInterval.overlap(timeToMatch) :: accum)
      else intervalsAccumulator(timeInterval.plusDays(1), accum)
    }
    intervalsAccumulator(dailyInterval.toRealTimeInterval(timeToMatch.getStart().toLocalDate()), Nil)
  }

  private def getRuleAllowedDays(timeToMatch: Interval, dailyInterval: LocalTimeInterval, weekDays: List[Int]): List[Interval] = {
    def intervalsAccumulator(date: DateTime, accum: List[Interval]): List[Interval] = {
      if(date.isAfter(timeToMatch.getEnd())) {
        accum
      }
      else {
        if(weekDays.contains(date.getDayOfWeek())) intervalsAccumulator(date + 1.days, 24.hours.toIntervalFrom(date) :: accum)
        else intervalsAccumulator(date + 1.days, accum)
      }
    }
    intervalsAccumulator(timeToMatch.getStart().withTime(0, 0, 0, 0), Nil)
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
}
