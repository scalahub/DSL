name := "DQL"

version := "0.1"

scalaVersion := "2.12.10"

lazy val commonResolvers = resolvers ++= Seq(
  "Sonatype Releases" at "https://s01.oss.sonatype.org/content/repositories/releases",
  "Sonatype Releases 2" at "https://oss.sonatype.org/content/repositories/releases/",
  "SonaType" at "https://oss.sonatype.org/content/groups/public",
  "SonaType Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/",
  "SonaType Staging" at "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
)

lazy val TrapCommon = (project in file("TrapCommon"))
  .settings(
    commonResolvers,
    libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.4",
    libraryDependencies += "io.github.scalahub" %% "beanshell-mod" % "1.0"
  )

lazy val DSLToXDSL = (project in file("DSLToXDSL"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4"
  )
  .dependsOn(TrapCommon)

lazy val DSLCommon = (project in file("DSLCommon"))
  .settings(
    commonResolvers,
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided",
    libraryDependencies += "io.github.scalahub" %% "beanshell-mod" % "1.0"
  )
  .dependsOn(TrapCommon)

lazy val XDSLLoader = (project in file("XDSLLoader"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
  )
  .dependsOn(TrapCommon, DSLCommon)

lazy val DatalogSolver = (project in file("DatalogSolver"))
  .settings(
    commonResolvers,
    // resolvers += Resolver.mavenLocal,
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "io.github.scalahub" %% "iris-reasoner-mod" % "1.0"
  )
  .dependsOn(
    XDSLLoader,
    TrapCommon
  )

lazy val XDSLToDatalog = (project in file("XDSLToDatalog"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4"
  )
  .dependsOn(XDSLLoader, DSLToXDSL)

lazy val DSLAnalyzer = (project in file("DSLAnalyzer"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4"
  )
  .dependsOn(
    XDSLToDatalog,
    DatalogSolver
  )

lazy val DQLDemo = (project in file("DQLDemo"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4",
    libraryDependencies += "com.opencsv" % "opencsv" % "3.8",
    libraryDependencies += "com.google.guava" % "guava" % "21.0",
    libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
    libraryDependencies += "com.google.jimfs" % "jimfs" % "1.1" % Test,
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.26"
  )
  .dependsOn(
    DSLAnalyzer
  )

lazy val root = (project in file("."))
  .settings(
    mainClass in assembly := Some("dql.DQLConsole"),
    name := "DQL"
  )
  .dependsOn(
    DQLDemo
  )

mainClass in (Compile, run) := Some("dql.DQLConsole")

/*
// https://github.com/sbt/sbt-assembly
Project.inConfig(Test)(baseAssemblySettings)
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "log4j.properties"                            => MergeStrategy.rename
  case PathList("META-INF", "MANIFEST.MF")	     => MergeStrategy.rename
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
 */
