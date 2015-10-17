package congestioncharge.domain.charging

case class Receipt(lines: List[ReceiptLine], total: BigDecimal)
