package congestioncharge.services

import org.joda.time._

class PriceCalculationService {

  def calculatePrice(period: Period, pricePerHour: BigDecimal): BigDecimal = {
    val pricePerSecond = pricePerHour / 3600
    (period.toStandardDuration().getStandardSeconds() * pricePerSecond).setScale(1, BigDecimal.RoundingMode.HALF_DOWN)
  }


}
