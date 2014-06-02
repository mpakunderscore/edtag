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

libraryDependencies += "com.google.apis" % "google-api-services-plus" % "v1-rev122-1.17.0-rc"

libraryDependencies += "com.google.api.client" % "google-api-client-json" % "1.2.3-alpha"

libraryDependencies += "com.google.http-client" % "google-http-client-jackson" % "1.18.0-rc"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.3.11"

closureCompilerOptions += "ecmascript5"
