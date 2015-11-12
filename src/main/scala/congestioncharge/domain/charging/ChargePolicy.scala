package congestioncharge.domain.charging

import congestioncharge.domain.core.VehicleType
import VehicleType._

case class ChargePolicy(vehicle: VehicleType, rules: List[ChargePolicyRule])
