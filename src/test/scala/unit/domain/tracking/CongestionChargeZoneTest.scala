package unit.domain.tracking

import congestioncharge.domain.charging._
import congestioncharge.domain.timing.TimingService
import congestioncharge.domain.tracking._
import congestioncharge.infrastructure._
import org.joda.time._
import org.mockito.Matchers.{eq => eqTo}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest._
import org.scalatest.mock.MockitoSugar

class CongestionChargeZoneTest extends FlatSpec with Matchers with Assertions with MockitoSugar {
  private val _chargeService = mock[ChargeService]
  private val _zoneRepository = mock[CongestionChargeZoneRepository]
  private val _zone = new CongestionChargeZone(_chargeService, _zoneRepository)
  private val _vehicle = new Vehicle("1", VehicleType.Car)

  "enter" should "write vehicle entry" in {
    when(_zoneRepository.hasVehicle(_vehicle.id)).thenReturn(false)

    _zone.enter(_vehicle)

    verify(_zoneRepository).writeVehicleEntry(eqTo(_vehicle.id), any[DateTime])
  }

  "enter" should "throw exception if vehicle is in the zone" in {
    when(_zoneRepository.hasVehicle(_vehicle.id)).thenReturn(true)

    intercept[UnsupportedOperationException] { _zone.enter(_vehicle) }
  }

  "leave" should "delete vehicle entry and return receipt" in {
    when(_zoneRepository.hasVehicle(_vehicle.id)).thenReturn(true)
    when(_zoneRepository.getVehicleEntryTime(_vehicle.id)).thenReturn(DateTime.now())
    when(_chargeService.charge(eqTo(_vehicle), any[Interval])).thenReturn(Receipt(Nil, 1))

    val receipt = _zone.leave(_vehicle)

    verify(_zoneRepository).dropVehicle(_vehicle.id)
    assert(receipt != null)
  }

  "leave" should "throw exception if vehicle is not in the zone" in {
    when(_zoneRepository.hasVehicle(_vehicle.id)).thenReturn(false)

    intercept[UnsupportedOperationException] { _zone.leave(_vehicle) }
  }

}
