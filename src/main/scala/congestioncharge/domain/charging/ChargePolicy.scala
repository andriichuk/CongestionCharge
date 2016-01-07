package congestioncharge.domain.charging

import congestioncharge.domain.shared.VehicleType
import VehicleType._

case class ChargePolicy(vehicle: VehicleType, rules: List[ChargePolicyRule])
