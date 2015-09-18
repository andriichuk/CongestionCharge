package congestioncharge.entities

case class Receipt(lines: List[ReceiptLine], total: BigDecimal)
