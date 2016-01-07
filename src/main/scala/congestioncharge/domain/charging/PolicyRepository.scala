package congestioncharge.domain.charging

import congestioncharge.domain.shared.VehicleType
import VehicleType.VehicleType

trait PolicyRepository {

  def getPolicy(vehicleType: VehicleType): ChargePolicy

}
