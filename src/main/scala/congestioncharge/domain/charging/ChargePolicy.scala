package congestioncharge.domain.charging

import congestioncharge.domain.tracking.VehicleType
import congestioncharge.domain.tracking.VehicleType._

case class ChargePolicy(vehicle: VehicleType, rules: List[ChargePolicyRule])
