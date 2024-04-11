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
  val changelog        = baseDirectory.value / "liquibase.xml"
  val logger           = streams.value.log

  def runLiquibase(url: String): Unit =
    //TODO Banner is appearing again. Probably needs working dir set.
    //TODO When Liquibase dies, it takes the sbt process with it. Fork?
    runner.value.run(cliMainClassName, classpathFiles,
      Seq("update", s"--changelog-file=$changelog", s"--url=$url", "--show-summary-output=LOG"),
      logger
    )
      .recover { case t: Throwable => sys.error(t.getMessage) }

  DatabaseUtilities.runWithDatabase(runLiquibase, logger)
}
