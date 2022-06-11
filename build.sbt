name := "DSL"

version := "1.0"

scalaVersion := "2.12.10"

lazy val commonResolvers = resolvers ++= Seq(
  "Sonatype Releases" at "https://s01.oss.sonatype.org/content/repositories/releases",
  "Sonatype Releases 2" at "https://oss.sonatype.org/content/repositories/releases/",
  "SonaType" at "https://oss.sonatype.org/content/groups/public",
  "SonaType Snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/",
  "SonaType Staging" at "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
)

lazy val dsl_util = (project in file("TrapCommon"))
  .settings(
    commonResolvers,
    libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.4",
    libraryDependencies += "io.github.scalahub" %% "beanshell-mod" % "1.0"
  )

lazy val dsl_to_xdsl = (project in file("DSLToXDSL"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4"
  )
  .dependsOn(dsl_util)

lazy val dsl_common = (project in file("DSLCommon"))
  .settings(
    commonResolvers,
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided",
    libraryDependencies += "io.github.scalahub" %% "beanshell-mod" % "1.0"
  )
  .dependsOn(dsl_util)

lazy val xdsl_loader = (project in file("XDSLLoader"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
  )
  .dependsOn(dsl_util, dsl_common)

lazy val datalog_solver = (project in file("DatalogSolver"))
  .settings(
    commonResolvers,
    // resolvers += Resolver.mavenLocal,
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "io.github.scalahub" %% "iris-reasoner-mod" % "1.0"
  )
  .dependsOn(
    xdsl_loader,
    dsl_util
  )

lazy val xdsl_to_datalog = (project in file("XDSLToDatalog"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4"
  )
  .dependsOn(xdsl_loader, dsl_to_xdsl)

lazy val dsl_analyzer = (project in file("DSLAnalyzer"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4"
  )
  .dependsOn(
    xdsl_to_datalog,
    datalog_solver
  )

lazy val dsl_demo = (project in file("DQLDemo"))
  .settings(
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    libraryDependencies += "commons-io" % "commons-io" % "2.6",
    libraryDependencies += "org.antlr" % "antlr" % "3.4",
    libraryDependencies += "com.opencsv" % "opencsv" % "3.8",
    libraryDependencies += "com.google.guava" % "guava" % "21.0",
    libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
    libraryDependencies += "com.google.jimfs" % "jimfs" % "1.1" % Test,
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.26",
    mainClass in run := Some("dql.DQLConsole")
  )
  .dependsOn(
    dsl_analyzer
  )

lazy val root = (project in file("."))
  .settings(
    publishArtifact := false,
    mainClass in assembly := Some("dql.DQLConsole"),
    name := "DSL"
  )
  .aggregate(
    dsl_demo,
    dsl_util,
    dsl_to_xdsl,
    xdsl_to_datalog,
    dsl_common,
    xdsl_loader,
    datalog_solver,
    dsl_analyzer
  )
  .dependsOn(
    dsl_demo
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
