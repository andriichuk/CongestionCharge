package unit

import congestioncharge.entities.{ReceiptPolicyRule, VehicleType, ReceiptPolicy}
import congestioncharge.services._
import congestioncharge.utils._
import org.joda.time._
import com.github.nscala_time.time.Imports._
import org.scalatest._

class PriceCalculationServiceTest extends FlatSpec with Matchers with Assertions {
  private val _service = new PriceCalculationService()

  "calculatePrice" should "correctly calculate price" in {
    assert(_service.calculatePrice(2.hours, 3.5) == 7)
  }

  "calculatePrice" should "round price to the nearest 10p" in {
    assert(_service.calculatePrice(20.minutes, 3.5) == 1.2)
  }
}
