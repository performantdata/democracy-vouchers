import _root_.caliban.tools.Codegen
import com.typesafe.sbt.packager.docker.DockerChmodType

import java.time.Instant

val calibanVer = "2.7.1"
val jacksonVersion = "2.17.0"
val tapirVersion = "1.10.8"

enablePlugins(BuildInfoPlugin, DockerPlugin, CalibanPlugin)

name := "democracy-vouchers-server"
description := "Back-end server for the \"democracy vouchers\" system."
libraryDependencies ++= Seq(
  // GraphQL and HTTP server
  "com.github.ghostdogpr" %% "caliban"        % calibanVer,
  "com.github.ghostdogpr" %% "caliban-http4s" % calibanVer,
  "com.github.ghostdogpr" %% "caliban-tapir"  % calibanVer,
  "org.http4s" %% "http4s-ember-server" % "0.23.27",
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % tapirVersion, //TODO Switch to OTel.
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % tapirVersion,

  // logging
  "org.typelevel" %% "log4cats-slf4j" % "2.6.0",
  "org.apache.logging.log4j" % "log4j-slf4j2-impl" % "2.23.1",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
  "com.fasterxml.jackson.core"       % "jackson-databind"        % jacksonVersion,

  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
  "com.softwaremill.sttp.client3" %% "circe" % "3.9.7" % Test
)

// sbt-buildinfo settings
buildInfoKeys := Seq(name, version, scalaVersion, sbtVersion)
buildInfoPackage := organization.value + ".voucher"

/* caliban-codegen-sbt settings
 * We follow a schema-first strategy: from a GraphQL schema, we generate the Scala types that describe the GraphQL API.
 */
Compile / caliban / calibanSettings += {
  calibanSetting(file("myproject/src/main/resources/voucher.graphql"))(
    // important to set this, otherwise you'll get client code
    _.genType(Codegen.GenType.Schema)
      // you can customize the codegen further with this DSL
      .clientName("VoucherApi.scala")
      .packageName("com.performantdata.voucher.schema")
  )
}

// sbt-native-packager settings

jlinkIgnoreMissingDependency := JlinkIgnore.only(
  "scala.quoted" -> "scala",
  "scala.quoted.runtime" -> "scala",
  "caliban" -> "com.github.plokhotnyuk.jsoniter_scala.core",
  "caliban" -> "play.api.libs.json",
  "caliban" -> "zio.json",
  "caliban.interop.jsoniter" -> "com.github.plokhotnyuk.jsoniter_scala.core",
  "caliban.interop.play" -> "play.api.libs.functional",
  "caliban.interop.play" -> "play.api.libs.functional.syntax",
  "caliban.interop.play" -> "play.api.libs.json",
  "caliban.interop.zio" -> "zio.json",
  "caliban.interop.zio" -> "zio.json.internal",
  "fs2.io.net.unixsocket" -> "jnr.unixsocket",
  "io.prometheus.metrics.tracer.otel" -> "io.opentelemetry.api.trace",
  "io.prometheus.metrics.tracer.otel_agent" -> "io.opentelemetry.javaagent.shaded.io.opentelemetry.api.trace",
  "izumi.reflect" -> "scala",
  "izumi.reflect.dottyreflection" -> "scala",
  "izumi.reflect.macrortti" -> "scala",
  "org.slf4j" -> "org.slf4j.impl",
  "sttp.tapir.macros" -> "sttp.tapir",
  "sttp.tapir.server" -> "sttp.tapir",
  "org.apache.logging.log4j.core.appender.mom" -> "javax.jms",
  "org.apache.logging.log4j.core.appender.mom.jeromq" -> "org.zeromq",
  "org.apache.logging.log4j.core.appender.mom.kafka" -> "org.apache.kafka.clients.producer",
  "org.apache.logging.log4j.core.appender.mom.kafka" -> "org.apache.kafka.common.serialization",
  "org.apache.logging.log4j.core.appender.rolling.action" -> "org.apache.commons.compress.compressors",
  "org.apache.logging.log4j.core.appender.rolling.action" -> "org.apache.commons.compress.utils",
  "org.apache.logging.log4j.core.async" -> "com.conversantmedia.util.concurrent",
  "org.apache.logging.log4j.core.async" -> "com.lmax.disruptor",
  "org.apache.logging.log4j.core.async" -> "com.lmax.disruptor.dsl",
  "org.apache.logging.log4j.core.async" -> "org.jctools.queues",
  "org.apache.logging.log4j.core.config.plugins.util" -> "org.osgi.framework",
  "org.apache.logging.log4j.core.config.plugins.util" -> "org.osgi.framework.wiring",
  "org.apache.logging.log4j.core.jackson" -> "com.fasterxml.jackson.dataformat.xml",
  "org.apache.logging.log4j.core.jackson" -> "com.fasterxml.jackson.dataformat.xml.annotation",
  "org.apache.logging.log4j.core.jmx" -> "com.lmax.disruptor",
  "org.apache.logging.log4j.core.layout" -> "com.fasterxml.jackson.dataformat.xml.annotation",
  "org.apache.logging.log4j.core.layout" -> "com.fasterxml.jackson.dataformat.xml.util",
  "org.apache.logging.log4j.core.layout" -> "org.apache.commons.csv",
  "org.apache.logging.log4j.core.layout" -> "org.codehaus.stax2",
  "org.apache.logging.log4j.core.net" -> "javax.activation",
  "org.apache.logging.log4j.core.net" -> "javax.mail",
  "org.apache.logging.log4j.core.net" -> "javax.mail.internet",
  "org.apache.logging.log4j.core.net" -> "javax.mail.util",
  "org.apache.logging.log4j.core.osgi" -> "org.osgi.framework",
  "org.apache.logging.log4j.core.osgi" -> "org.osgi.framework.wiring",
  "org.apache.logging.log4j.core.pattern" -> "org.fusesource.jansi",
  "org.apache.logging.log4j.util" -> "org.osgi.framework",
  "org.apache.logging.log4j.util" -> "org.osgi.framework.wiring",
)

makeBatScripts := Seq.empty
dockerBaseImage := "debian:12.4-slim"
dockerLabels ++= Map(
  "org.opencontainers.image.created"       -> Instant.now.toString,
  "org.opencontainers.image.authors"       -> "https://github.com/performantdata/",
  "org.opencontainers.image.url"           -> "https://github.com/performantdata/democracy-vouchers/tree/master/server/",
  "org.opencontainers.image.documentation" -> "https://github.com/performantdata/democracy-vouchers/blob/master/server/README.md",
  "org.opencontainers.image.source"        -> "https://github.com/performantdata/democracy-vouchers/tree/master/server/",
  "org.opencontainers.image.version"       -> version.value,
  "org.opencontainers.image.revision"      -> git.gitHeadCommit.value.getOrElse(""),
  "org.opencontainers.image.vendor"        -> organizationName.value,
  "org.opencontainers.image.title"         -> "Democracy vouchers test system",
  "org.opencontainers.image.description"   -> description.value.replace("\"", "\\\""),
  "org.opencontainers.image.base.name"     -> dockerBaseImage.value,
)
dockerEnvVars ++= Map("LANG" -> "C.UTF-8")
dockerExposedPorts := Seq(9000, 9443)
dockerUpdateLatest := true
// We need a writable, searchable directory for log files.
dockerAdditionalPermissions +=
  (DockerChmodType.UserGroupWriteExecute, (Docker / defaultLinuxInstallLocation).value + "/logs")

// Map our special tasks to the DockerPlugin ones from sbt-native-packager.
dockerBuild := {
  (Docker / publishLocal).value
}
dockerPublish := {
  (Docker / publish).value
}
