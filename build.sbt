name := "DQL"

version := "0.1"

scalaVersion := "2.12.8"

lazy val BeanShellGitRepo = "git://github.com/scalahub/BeanShell.git"
lazy val BeanShell = RootProject(uri(BeanShellGitRepo))


lazy val TrapCommon = (project in file("TrapCommon")).dependsOn(BeanShell).settings(
  libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
  // https://mvnrepository.com/artifact/commons-io/commons-io
  libraryDependencies += "commons-io" % "commons-io" % "2.6",
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
)

lazy val DSLCommon = (project in file("DSLCommon")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"
).dependsOn(BeanShell, TrapCommon)

lazy val XDSLLoader = (project in file("XDSLLoader")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
).dependsOn(TrapCommon, DSLCommon)

//https://stackoverflow.com/a/38917522/243233
lazy val IrisReasonerGitRepo = "git://github.com/scalahub/iris-reasoner.git"
lazy val IrisReasoner = RootProject(uri(IrisReasonerGitRepo))

lazy val DatalogSolver = (project in file("DatalogSolver")).settings(
  libraryDependencies += "commons-io" % "commons-io" % "2.6 "
).dependsOn(XDSLLoader, TrapCommon, DSLCommon, IrisReasoner)

lazy val DSLToXDSL = (project in file("DSLToXDSL")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  // https://mvnrepository.com/artifact/org.antlr/antlr
  libraryDependencies += "org.antlr" % "antlr" % "3.4"
).dependsOn(TrapCommon)


lazy val XDSLToDatalog = (project in file("XDSLToDatalog")).settings(
  //libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  // https://mvnrepository.com/artifact/org.antlr/antlr
  libraryDependencies += "org.antlr" % "antlr" % "3.4"
).dependsOn(TrapCommon, DSLCommon, XDSLLoader, DSLToXDSL)


lazy val DSLAnalyzer = (project in file("DSLAnalyzer")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  // https://mvnrepository.com/artifact/commons-io/commons-io
  libraryDependencies += "commons-io" % "commons-io" % "2.6",
  libraryDependencies += "org.antlr" % "antlr" % "3.4"
).dependsOn(TrapCommon, DSLCommon, XDSLLoader, DSLToXDSL, XDSLToDatalog, IrisReasoner, DatalogSolver)


lazy val DQLDemo = (project in file("DQLDemo")).settings(
  libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
  // https://mvnrepository.com/artifact/commons-io/commons-io
  libraryDependencies += "commons-io" % "commons-io" % "2.6",
  libraryDependencies += "org.antlr" % "antlr" % "3.4",
  // https://mvnrepository.com/artifact/com.opencsv/opencsv
  libraryDependencies += "com.opencsv" % "opencsv" % "3.8",
  // https://mvnrepository.com/artifact/com.google.guava/guava
  //libraryDependencies += "com.google.guava" % "guava" % "21.0",
  libraryDependencies += "commons-codec" % "commons-codec" % "1.12",
  // https://mvnrepository.com/artifact/com.google.jimfs/jimfs
  libraryDependencies += "com.google.jimfs" % "jimfs" % "1.1" % Test,
  // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
  libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.26"
).dependsOn(TrapCommon, DSLAnalyzer, DSLCommon, XDSLLoader, DSLToXDSL, XDSLToDatalog, IrisReasoner, BeanShell)

lazy val root = (project in file(".")).aggregate(DQLDemo).settings(
  // set the name of the project
  name := "DQL"
).dependsOn(
  DQLDemo
)

mainClass in (Compile, run) := Some("dql.DQLConsole")
