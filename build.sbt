name := """$name$"""
organization := "$organization$"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

libraryDependencies += guice
libraryDependencies += ehcache
libraryDependencies += ws
libraryDependencies += filters
libraryDependencies += "com.mohiva" %% "play-silhouette" % "5.0.0"
libraryDependencies += "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0"
libraryDependencies += "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.0"
libraryDependencies += "com.mohiva" %% "play-silhouette-persistence" % "5.0.0"
libraryDependencies += "com.mohiva" %% "play-silhouette-testkit" % "5.0.0" % "test"
libraryDependencies += "com.mohiva" %% "play-silhouette-cas" % "5.0.0"
libraryDependencies += "com.mohiva" %% "play-silhouette-persistence-reactivemongo" % "5.0.0"

libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.1"
libraryDependencies += "com.iheart" %% "ficus" % "1.4.3"

libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.12.3"

libraryDependencies += "com.typesafe.play" % "play-json-joda_2.12" % "2.6.0"

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"

libraryDependencies += "io.swagger" %% "swagger-play2" % "1.6.1-SNAPSHOT"

libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3"
libraryDependencies += "org.webjars" %% "webjars-play" % "2.6.1"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery")
libraryDependencies += "org.webjars" % "jquery" % "3.2.1"

libraryDependencies += "com.google.oauth-client" % "google-oauth-client" % "1.23.0"

libraryDependencies += "com.mohiva" %% "play-silhouette-testkit" % "5.0.0" % "test"
libraryDependencies += specs2 % Test

resolvers += Resolver.jcenterRepo
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "atlassian-maven" at "https://maven.atlassian.com/content/repositories/atlassian-public"