# Actor System for Congestion Charge Problem

Performs congestion charge calculations via akka actors. 

## Usage

You may either run calculation via domain service, providing your own receipt policies:

```scala
val carAmRule = ReceiptPolicyRule("AM rate", 2, WeekDays.WorkWeek, LocalTimeInterval(new LocalTime(7, 0), new LocalTime(12, 0)))
val carPmRule = ReceiptPolicyRule("PM rate", 2.5, WeekDays.WorkWeek, LocalTimeInterval(new LocalTime(12, 0), new LocalTime(19, 0)))
val carPolicy = ReceiptPolicy(VehicleType.Car, List(carAmRule, carPmRule))
val service = new ReceiptBuildingService()
val receipt = service.buildReceipt(_carPolicy, new DateTime(2008, 4, 24, 11, 32) to new DateTime(2008, 4, 24, 14, 42))
```

... or use an actor, which will fetch policies from the repository:

```scala
val system = ActorSystem("CongestionChargeSystem")
implicit val timeout = Timeout(10 seconds)
implicit val executionContext = system.dispatcher

val receiptBuilder = system.actorOf(Props[ReceiptBuilder])
val interval = new Interval(DateTime.now().minusDays(2).getMillis(), DateTime.now().getMillis())
val future = receiptBuilder ? BuildReceipt(VehicleType.Car, interval)
val receipt = Await.result(future, timeout.duration).asInstanceOf[Receipt]
println(s"Receipt received! Total: ${receipt.total}")
```

## Evidence

The evidence that solution resolves the Congestion Charge Problem is provided in [ReceiptBuildingServiceTest](https://github.com/andriichuk/CongestionCharge/blob/master/src/test/scala/unit/ReceiptBuildingServiceTest.scala). 

You can run the tests as follows:
```sh
sbt test
```

Also you can run the actor system, getting output for sample data as follows:
```sh
sbt run
```