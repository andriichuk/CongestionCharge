package congestioncharge.domain.tracking

import org.joda.time.DateTime

trait CongestionChargeZoneRepository {

  def writeVehicleEntry(vehicleId: String, time: DateTime)

  def hasVehicle(vehicleId: String): Boolean

  def getVehicleEntryTime(vehicleId: String) : DateTime

  def dropVehicle(vehicleId: String)

}
