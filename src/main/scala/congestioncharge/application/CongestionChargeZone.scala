package congestioncharge.application

import congestioncharge.domain.charging._
import congestioncharge.domain.core.Vehicle
import congestioncharge.domain.tracking._

class CongestionChargeZone(trackingService: TrackingService, chargeService: ChargeService) {

  def enter(vehicle: Vehicle): Unit = trackingService.enter(vehicle)

  def leave(vehicle: Vehicle): Receipt = chargeService.charge(vehicle, trackingService.leave(vehicle))

}
