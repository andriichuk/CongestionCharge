package congestioncharge.infrastructure

import congestioncharge.domain.tracking.CongestionChargeZoneRepository
import org.joda.time.DateTime

class CongestionChargeZoneMemoryRepository extends CongestionChargeZoneRepository {

  private val _memoryDb = scala.collection.mutable.Map[String, DateTime]()

  override def writeVehicleEntry(vehicleId: String, time: DateTime): Unit = _memoryDb += "vehicleId" -> time

  override def hasVehicle(vehicleId: String): Boolean = _memoryDb.contains(vehicleId)

  override def getVehicleEntryTime(vehicleId: String): DateTime = _memoryDb(vehicleId)

  override def dropVehicle(vehicleId: String): Unit = _memoryDb.remove(vehicleId)

}
