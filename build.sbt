name := "DQL"

version := "0.1"

scalaVersion := "2.12.8"

lazy val BeanShell = RootProject(uri("https://github.com/scalahub/BeanShell.git"))
//lazy val BeanShell = RootProject(uri("../BeanShell"))

lazy val TrapCommon = (project in file("TrapCommon")).settings(
  libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
  libraryDependencies += "commons-io" % "commons-io" % "2.6",
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
).dependsOn(BeanShell)

lazy val DSLToXDSL = (project in file("DSLToXDSL")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  libraryDependencies += "org.antlr" % "antlr" % "3.4"
).dependsOn(TrapCommon)

lazy val DSLCommon = (project in file("DSLCommon")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"
).dependsOn(BeanShell, TrapCommon)

lazy val XDSLLoader = (project in file("XDSLLoader")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
).dependsOn(TrapCommon, DSLCommon)

lazy val IrisReasoner = RootProject(uri("https://github.com/scalahub/iris-reasoner.git"))
//To refer to a local repository
//lazy val IrisReasoner = RootProject(uri("../iris-reasoner"))

// Sub-projects can be accessed as follows:
// From https://stackoverflow.com/a/38917522/243233
//lazy val IrisParser = ProjectRef(uri("git://github.com/scalahub/iris-reasoner.git"), "parser")
//lazy val IrisParser = ProjectRef(uri("../iris-reasoner"), "parser")

lazy val DatalogSolver = (project in file("DatalogSolver")).settings(
  libraryDependencies += "commons-io" % "commons-io" % "2.6 "
).dependsOn(
  XDSLLoader, 
  TrapCommon,
  //IrisParser,
  IrisReasoner
)


lazy val XDSLToDatalog = (project in file("XDSLToDatalog")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  libraryDependencies += "org.antlr" % "antlr" % "3.4"
).dependsOn(XDSLLoader, DSLToXDSL)


lazy val DSLAnalyzer = (project in file("DSLAnalyzer")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  libraryDependencies += "commons-io" % "commons-io" % "2.6",
  libraryDependencies += "org.antlr" % "antlr" % "3.4"
).dependsOn(
  XDSLToDatalog, 
  DatalogSolver
)


lazy val DQLDemo = (project in file("DQLDemo")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  libraryDependencies += "commons-io" % "commons-io" % "2.6",
  libraryDependencies += "org.antlr" % "antlr" % "3.4",
  libraryDependencies += "com.opencsv" % "opencsv" % "3.8",
  libraryDependencies += "com.google.guava" % "guava" % "21.0",
  libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
  libraryDependencies += "com.google.jimfs" % "jimfs" % "1.1" % Test,
  libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.26"
).dependsOn(
  DSLAnalyzer
)

lazy val root = (project in file(".")).settings(
  mainClass in assembly := Some("dql.DQLConsole"),
  name := "DQL"
).dependsOn(
  DQLDemo
)
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

mainClass in (Compile, run) := Some("dql.DQLConsole")
