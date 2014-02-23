import sbt._
import Keys._
import sbtappengine._
import Plugin._
import AppengineKeys._


object GaeBuild extends Build {
  name := "gae-experiment"

  lazy val buildSettings = Seq(
    organization := "xp5t5r.experiments",
    version := "0.1",
    scalaVersion := "2.10.3",
    scalacOptions ++= Seq("-deprecation", "-feature")
  )

  lazy val defaultSettings = {
    Defaults.defaultSettings ++ buildSettings/* ++ Seq(
      libraryDependencies ++= Seq(
        "junit" % "junit" % "4.8.2" % "test",
        "org.scalatest" %% "scalatest" % "1.6.1" % "test"
      )
    )*/
  }

  lazy val root = Project(
    id = "gae-experiment-root",
    base = file("."),
    settings = defaultSettings ++ appengineSettings ++ Seq(
      libraryDependencies ++= Seq(
        "net.databinder" %% "unfiltered-filter" % "0.6.8",
        "net.databinder" %% "unfiltered-json4s" % "0.6.8",
        "javax.servlet" % "servlet-api" % "2.5" % "provided",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.11.v20130520" % "container"
      )
    )
  )
}

