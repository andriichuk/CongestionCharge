package unit.domain.tracking

import congestioncharge.domain.charging._
import congestioncharge.domain.core.{VehicleType, Vehicle}
import congestioncharge.domain.timing.TimingService
import congestioncharge.domain.tracking._
import congestioncharge.infrastructure._
import org.joda.time._
import org.mockito.Matchers.{eq => eqTo}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar

class TrackingServiceTest extends FlatSpec with Matchers with Assertions with MockitoSugar {
  private val _trackingRepository = mock[TrackingRepository]
  private val _trackingService = new TrackingService(_trackingRepository)
  private val _vehicle = new Vehicle("1", VehicleType.Car)

  "enter" should "write vehicle entry" in {
    when(_trackingRepository.hasVehicle(_vehicle.id)).thenReturn(false)

    _trackingService.enter(_vehicle)

    verify(_trackingRepository).writeVehicleEntry(eqTo(_vehicle.id), any[DateTime])
  }

  "enter" should "throw exception if vehicle is in the zone" in {
    when(_trackingRepository.hasVehicle(_vehicle.id)).thenReturn(true)

    intercept[UnsupportedOperationException] { _trackingService.enter(_vehicle) }
  }

  "leave" should "delete vehicle entry and return time interval" in {
    when(_trackingRepository.hasVehicle(_vehicle.id)).thenReturn(true)
    when(_trackingRepository.getVehicleEntryTime(_vehicle.id)).thenReturn(DateTime.now())

    val interval = _trackingService.leave(_vehicle)

    verify(_trackingRepository).dropVehicle(_vehicle.id)
    assert(interval != null)
  }

  "leave" should "throw exception if vehicle is not in the zone" in {
    when(_trackingRepository.hasVehicle(_vehicle.id)).thenReturn(false)

    intercept[UnsupportedOperationException] { _trackingService.leave(_vehicle) }
  }

}
