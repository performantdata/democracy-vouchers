ThisBuild / organization := "com.performantdata"
ThisBuild / organizationName := "Performant Data LLC"
ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / scalaVersion := "3.4.2"
ThisBuild / scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Wnonunit-statement",
)

/** Subproject for the static website generation. */
lazy val `static` = project

/** Subproject for the backend database interfacing library. */
lazy val database = project
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % Versions.slick,
    )
  )

/** Subproject for the backend server. */
lazy val server = project
  .dependsOn(database)
  .enablePlugins(BuildInfoPlugin, DockerPlugin, CalibanPlugin)

lazy val root = (project in file("."))
  .aggregate(database, server, `static`)
  .settings(
    /* Don't duplicate `dependsOn` dependencies between the subprojects.  We don't care that the "compile" task won't
     * be replicated to the subprojects. */
    compile / aggregate := false,
  )
  .enablePlugins(GitVersioning, JlinkPlugin, JavaAppPackaging)
  .settings(
    name := "democracy-vouchers",
    description := "A test implementation of a \"democracy vouchers\" campaign financing system.",
//    idePackagePrefix := Some(organization.value + ".voucher")
  )
