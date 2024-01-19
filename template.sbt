/* Build script related to the template example code.
 *
 * It should not be necessary for users of this template project to modify this code.
 */
ThisBuild / libraryDependencies ++= {
  val calibanVersion = "2.5.1"
  val jacksonVersion = "2.15.2"
  val tapirVersion = "1.9.6"

  Seq(
    // GraphQL and HTTP server
    "com.github.ghostdogpr" %% "caliban"        % calibanVersion,
    "com.github.ghostdogpr" %% "caliban-http4s" % calibanVersion,
    "com.github.ghostdogpr" %% "caliban-tapir"  % calibanVersion,
    "org.http4s" %% "http4s-ember-server" % "0.23.25",
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % tapirVersion,

    // logging
    "org.apache.logging.log4j" % "log4j-slf4j2-impl" % "2.22.1",
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
    "com.fasterxml.jackson.core"       % "jackson-databind"        % jacksonVersion,

    "org.scalatest" %% "scalatest" % "3.2.17" % Test,
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
    "com.softwaremill.sttp.client3" %% "circe" % "3.9.2" % Test
  )
}
