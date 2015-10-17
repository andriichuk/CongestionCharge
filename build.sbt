name := """congestion-charge"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "com.github.nscala-time" %% "nscala-time" % "2.2.0",
  "org.mockito" % "mockito-core" % "1.9.5"
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
