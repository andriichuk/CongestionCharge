package congestioncharge.domain.charging

import congestioncharge.domain.tracking.VehicleType
import congestioncharge.domain.tracking.VehicleType.VehicleType

trait PolicyRepository {

  def getPolicy(vehicleType: VehicleType): ChargePolicy

}
