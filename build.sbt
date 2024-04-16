ThisBuild / organization := "com.performantdata"
ThisBuild / organizationName := "Performant Data LLC"
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
)

/** Static Web files subproject. */
lazy val `static` = project
  .settings(
    // These setting are made here so that the subproject looks more like a typical NPM project, not an sbt one.
    name := "democracy-vouchers-static",
    description := "Static Web files for the \"democracy vouchers\" system.",
  )

lazy val database = project

lazy val server = project
  .dependsOn(database)
  .enablePlugins(DockerPlugin)

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, GitVersioning, JlinkPlugin, JavaAppPackaging)
  .settings(
    name := "democracy-vouchers",
    description := "A test implementation of a \"democracy vouchers\" campaign financing system.",
//    idePackagePrefix := Some(organization.value + ".voucher")

    // sbt-buildinfo settings
    buildInfoKeys := Seq(name, version, scalaVersion, sbtVersion),
    buildInfoPackage := organization.value + ".voucher",
  )
