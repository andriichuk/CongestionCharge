package congestioncharge.domain.charging

import congestioncharge.domain.shared.LocalTimeInterval

case class ChargePolicyRule(name: String, pricePerHour: BigDecimal, weekDays: List[Int], timeInterval: LocalTimeInterval)
