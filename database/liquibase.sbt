/* Build script for Liquibase
 *
 * Doesn't depend on the command-line version of Liquibase being present; downloads the JAR automatically instead.
 */

/** Sbt configuration for running Liquibase. */
lazy val LiquibaseConfig = config("liquibase").hide

ivyConfigurations += LiquibaseConfig
libraryDependencies ++= Seq(
  "org.liquibase" % "liquibase-core" % Versions.liquibase,
  "info.picocli" % "picocli" % "4.7.5",
  // each of the supported databases:
  "org.postgresql" % "postgresql" % "42.7.3",
).map(_ % LiquibaseConfig.name)

// Fetch the needed classpath (JARs).
LiquibaseConfig / managedClasspath := {
  val artifactTypes = (LiquibaseConfig / classpathTypes).value
  Classpaths.managedJars(LiquibaseConfig, artifactTypes, update.value)
}

lazy val liquibase = taskKey[Unit]("Run Liquibase")
liquibase := {
  val cliMainClassName = "liquibase.integration.commandline.LiquibaseCommandLine"
  val classpathFiles   = (LiquibaseConfig / managedClasspath).value.files
  val thisDir          = baseDirectory.value
  val logger           = streams.value.log

  def runLiquibase(url: String, db: Database, adminPassword: String): Unit = {
    val args = s"--search-path=$thisDir" :: s"--defaults-file=$thisDir/liquibase.properties" ::
      "update" :: s"--log-file=${target.value}/liquibase.log" ::
      s"--url=$url" :: s"--username=${db.adminUser}" :: s"--password=$adminPassword" :: Nil

    runner.value.run(cliMainClassName, classpathFiles, args, logger)
      .recover { case t: Throwable => sys.error(t.getMessage) }
  }

  DatabaseUtilities.runWithDatabase(runLiquibase, logger)
}
