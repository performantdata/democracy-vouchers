import com.typesafe.sbt.packager.docker.DockerChmodType

import java.time.Instant

ThisBuild / organization := "com.performantdata"
ThisBuild / organizationName := "Performant Data LLC"
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-target:17",
  "-deprecation",
  "-feature",
  "-unchecked",
)

/** Static Web files subproject. */
lazy val `static` = (project in file("static"))
  .settings(
    name := "democracy-vouchers-static",
    description := "Static Web files for the \"democracy vouchers\" system.",
  )

lazy val database = project

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, DockerPlugin, GitVersioning, JlinkPlugin, JavaAppPackaging)
  .settings(
    name := "democracy-vouchers",
    description := "A test implementation of a \"democracy vouchers\" campaign financing system.",
//    idePackagePrefix := Some(organization.value + ".voucher")

    // sbt-buildinfo settings
    buildInfoKeys := Seq(name, version, scalaVersion, sbtVersion),
    buildInfoPackage := organization.value + ".voucher",

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
    ),
    makeBatScripts := Seq.empty,
    dockerBaseImage := "debian:12.4-slim",
    dockerLabels ++= Map(
      "org.opencontainers.image.created"       -> Instant.now.toString,
      "org.opencontainers.image.authors"       -> "https://github.com/performantdata/",
      "org.opencontainers.image.url"           -> "https://github.com/performantdata/democracy-vouchers/",
      "org.opencontainers.image.documentation" -> "https://github.com/performantdata/democracy-vouchers/README.md",
      "org.opencontainers.image.source"        -> "https://github.com/performantdata/democracy-vouchers/",
      "org.opencontainers.image.version"       -> version.value,
      "org.opencontainers.image.revision"      -> git.gitHeadCommit.value.getOrElse(""),
      "org.opencontainers.image.vendor"        -> organizationName.value,
      "org.opencontainers.image.title"         -> "Democracy vouchers test system",
      "org.opencontainers.image.description"   -> description.value.replace("\"", "\\\""),
      "org.opencontainers.image.base.name"     -> dockerBaseImage.value,
    ),
    dockerEnvVars ++= Map("LANG" -> "C.UTF-8"),
    dockerExposedPorts := Seq(9000, 9443),
    dockerUsername := Some("performantdata"),
    dockerUpdateLatest := true,
    // We need a writable, searchable directory for log files.
    dockerAdditionalPermissions +=
      (DockerChmodType.UserGroupWriteExecute, (Docker / defaultLinuxInstallLocation).value + "/logs"),
  )
