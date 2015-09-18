package congestioncharge.services

import congestioncharge.entities._
import congestioncharge.utils.IntervalImplicits._
import org.joda.time._

class ReceiptBuildingService {

  // TODO: IoC
  val timeMatchingService = new TimeMatchingService()
  val priceCalculationService = new PriceCalculationService()

  def buildReceipt(policy: ReceiptPolicy, trackedTime: Interval): Receipt = {
    val receiptLines = policy.rules map { buildReceiptLine(_, trackedTime) }
    Receipt(receiptLines, receiptLines.foldLeft(0: BigDecimal)((total, line) => total + line.total))
  }

  def buildReceiptLine(rule: ReceiptPolicyRule, trackedTime: Interval): ReceiptLine = {
    val totalPeriod = timeMatchingService.getTotalTimeMatchingPolicyRule(trackedTime, rule)
    ReceiptLine(totalPeriod,
      priceCalculationService.calculatePrice(totalPeriod, rule.pricePerHour),
      rule.name)
  }

}
