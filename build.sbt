import play.Project._

name := "server"

version := "1.0"

// javacOptions ++= Seq("-source", "1.7")

playJavaSettings

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3"

libraryDependencies += jdbc

libraryDependencies += cache

libraryDependencies += javaEbean

// resolvers += Resolver.url("sbt-plugin-releases", url("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

// libraryDependencies += "ws.securesocial" %% "securesocial" % "2.1.3"

libraryDependencies += "postgresql" % "postgresql" % "9.1-901.jdbc4"

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.3"