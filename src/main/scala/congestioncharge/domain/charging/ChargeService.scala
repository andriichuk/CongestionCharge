package congestioncharge.domain.charging

import congestioncharge.domain.core.Vehicle
import congestioncharge.domain.timing.TimingService
import org.joda.time._

class ChargeService(timingService: TimingService, policyRepository: PolicyRepository) {

  private val _timingService = timingService
  private val _policyRepository = policyRepository

  def charge(vehicle: Vehicle, trackedTime: Interval): Receipt = {
    val policy = _policyRepository.getPolicy(vehicle.vehicleType)
    val receiptLines = policy.rules map { buildReceiptLine(_, trackedTime) }
    Receipt(receiptLines, receiptLines.foldLeft(0: BigDecimal)((total, line) => total + line.total))
  }

  private def buildReceiptLine(rule: ChargePolicyRule, trackedTime: Interval): ReceiptLine = {
    val matchingIntervals = _timingService.matchTimeIntervalToDailyIntervals(trackedTime, rule.timeInterval, rule.weekDays)
    val totalPeriod = _timingService.sumUpIntervals(matchingIntervals)
    ReceiptLine(totalPeriod, calculatePrice(totalPeriod, rule.pricePerHour), rule.name)
  }

  private def calculatePrice(period: Period, pricePerHour: BigDecimal): BigDecimal = {
    val pricePerSecond = pricePerHour / 3600
    (period.toStandardDuration().getStandardSeconds() * pricePerSecond).setScale(1, BigDecimal.RoundingMode.HALF_DOWN)
  }

}
