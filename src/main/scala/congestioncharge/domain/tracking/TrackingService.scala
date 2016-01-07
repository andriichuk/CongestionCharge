package congestioncharge.domain.tracking

import com.github.nscala_time.time.Imports._

class TrackingService(trackingRepository: TrackingRepository) {

  private val _trackingRepository = trackingRepository

  def enter(vehicle: Vehicle): Unit = {
    if(_trackingRepository.hasVehicle(vehicle.id)) {
      throw new UnsupportedOperationException("Vehicle tries to enter but it didn't leave")
    }

    _trackingRepository.writeVehicleEntry(vehicle.id, DateTime.now())
  }

  def leave(vehicle: Vehicle): Interval = {
    if(!_trackingRepository.hasVehicle(vehicle.id)) {
      throw new UnsupportedOperationException("Vehicle wasn't recorded on entry or already left")
    }

    val entryTime = _trackingRepository.getVehicleEntryTime(vehicle.id)
    _trackingRepository.dropVehicle(vehicle.id)
    entryTime to DateTime.now()
  }

}
