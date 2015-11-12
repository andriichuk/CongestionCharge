package congestioncharge.domain.charging

import congestioncharge.domain.core.VehicleType
import VehicleType.VehicleType

trait PolicyRepository {

  def getPolicy(vehicleType: VehicleType): ChargePolicy

}
