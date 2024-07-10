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

ThisBuild / dockerUsername := Some("performantdata")

/** Subproject for the static website generation. */
lazy val `static` = project

/** Subproject for the backend database interfacing library. */
lazy val database = project

/** Subproject for the backend server. */
lazy val server = project
  .dependsOn(database)

lazy val root = (project in file("."))
  .settings(
    name := "democracy-vouchers",
    description := "A test implementation of a \"democracy vouchers\" campaign financing system.",
//    idePackagePrefix := Some(organization.value + ".voucher")

    /* Don't duplicate `dependsOn` dependencies between the subprojects.  We don't care that the "compile" task won't
     * be replicated to the subprojects. */
    compile / aggregate := false,

    publish / skip := true,
    publishLocal / skip := true,
    publishM2 / skip := true,

    dockerBuild := {
      (server / dockerBuild).value
      (`static` / dockerBuild).value
    },
    dockerPublish := {
      (server / dockerPublish).value
      (`static` / dockerPublish).value
    },
  )
