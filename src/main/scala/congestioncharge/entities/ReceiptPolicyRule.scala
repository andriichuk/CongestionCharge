package congestioncharge.entities

import congestioncharge.utils._

case class ReceiptPolicyRule(name: String, pricePerHour: BigDecimal, weekDays: List[Int], timeInterval: LocalTimeInterval)
