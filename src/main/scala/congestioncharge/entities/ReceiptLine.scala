package congestioncharge.entities

import org.joda.time._

case class ReceiptLine (period: Period, total: BigDecimal, title: String)
