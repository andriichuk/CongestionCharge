package congestioncharge.domain.tracking

import congestioncharge.domain.charging.{Receipt, ChargeService}
import com.github.nscala_time.time.Imports._

class CongestionChargeZone(chargeService: ChargeService, zoneRepository: CongestionChargeZoneRepository) {

  private val _chargeService = chargeService
  private val _zoneRepository = zoneRepository

  def enter(vehicle: Vehicle): Unit = {
    if(_zoneRepository.hasVehicle(vehicle.id)) {
      throw new UnsupportedOperationException("Vehicle tries to enter but it didn't leave")
    }

    _zoneRepository.writeVehicleEntry(vehicle.id, DateTime.now())
  }

  def leave(vehicle: Vehicle): Receipt = {
    if(!_zoneRepository.hasVehicle(vehicle.id)) {
      throw new UnsupportedOperationException("Vehicle wasn't recorded on entry or already left")
    }

    val entryTime = _zoneRepository.getVehicleEntryTime(vehicle.id)
    _zoneRepository.dropVehicle(vehicle.id)
    _chargeService.charge(vehicle, entryTime to DateTime.now())
  }

}
