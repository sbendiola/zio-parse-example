val scala3Version = "3.1.2"

val zioVersion = "2.0.0-RC6"

lazy val root = project
  .in(file("."))
  .settings(
    name := "poseidonz",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    libraryDependencies ++= Seq(
      "dev.zio"               %% "zio"          % zioVersion,
      "dev.zio" %% "zio-parser" % "0.1.6",
      "dev.zio" %% "zio-test" % zioVersion % "test",
      "dev.zio" %% "zio-test-sbt"   % zioVersion % "test"))
