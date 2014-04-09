import play.Project._

name := "server"

version := "1.0"

// javacOptions ++= Seq("-source", "1.7")

playJavaSettings

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3"

libraryDependencies += jdbc

libraryDependencies += cache

libraryDependencies += javaEbean

libraryDependencies += "postgresql" % "postgresql" % "9.1-901.jdbc4"

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.3"

libraryDependencies += "com.google.api-client" % "google-api-client" % "1.17.0-rc"