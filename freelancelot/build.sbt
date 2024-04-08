name := """freelancelot"""
organization := "soen.hacs"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.8"

libraryDependencies += guice

// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.9.0"
// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-params" % "5.8.2" % Test
// https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-params
libraryDependencies += "org.junit.jupiter" % "junit-jupiter-params" % "5.8.2" % Test

// https://mvnrepository.com/artifact/org.mockito/mockito-all
libraryDependencies += "org.mockito" % "mockito-all" % "1.10.19" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.6.18" % Test

libraryDependencies ++= Seq(
  caffeine
)