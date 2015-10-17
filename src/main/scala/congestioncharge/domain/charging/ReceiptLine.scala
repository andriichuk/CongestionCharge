package congestioncharge.domain.charging

import org.joda.time._

case class ReceiptLine (period: Period, total: BigDecimal, title: String)
