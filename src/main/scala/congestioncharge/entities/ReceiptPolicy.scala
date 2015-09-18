package congestioncharge.entities

import VehicleType._

case class ReceiptPolicy(vehicle: VehicleType, rules: List[ReceiptPolicyRule])
