package congestioncharge.domain.charging

import congestioncharge.domain.timing.LocalTimeInterval

case class ChargePolicyRule(name: String, pricePerHour: BigDecimal, weekDays: List[Int], timeInterval: LocalTimeInterval)
