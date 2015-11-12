package unit.application

import congestioncharge.application._
import congestioncharge.domain.charging._
import congestioncharge.domain.core.{VehicleType, Vehicle}
import congestioncharge.domain.tracking._
import org.joda.time.DateTime
import org.mockito.Matchers.{eq => eqTo}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock
import org.scalatest.mock.MockitoSugar
import org.scalatest._
import com.github.nscala_time.time.Imports._

class CongestionChargeZoneTest extends FlatSpec with Matchers with Assertions with MockitoSugar {

  private val _trackingService = mock[TrackingService]
  private val _chargeService = mock[ChargeService]
  private val _zone = new CongestionChargeZone(_trackingService, _chargeService)
  private val _vehicle = new Vehicle("1", VehicleType.Car)
  private val _receipt = Receipt(List(ReceiptLine(new Period(0), 10, "Receipt line")), 10)

  "enter" should "track vehicle entry" in {
    _zone.enter(_vehicle)

    verify(_trackingService).enter(eqTo(_vehicle))
  }

  "leave" should "delete vehicle entry and return receipt" in {
    when(_trackingService.leave(eqTo(_vehicle))).thenReturn(new Interval(1, 1))
    when(_chargeService.charge(eqTo(_vehicle), any[Interval])).thenReturn(_receipt)

    val receipt = _zone.leave(_vehicle)

    assert(receipt == _receipt)
    verify(_trackingService).leave(eqTo(_vehicle))
  }
}
