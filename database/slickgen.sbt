// Build script for Slick code generation

/** JDBC libraries of the supported databases. */
val dbLibraries = Seq(
  "org.postgresql" % "postgresql" % "42.7.3",
)

// Generate the code to the normal source location, so that it's commited to the VCS.
(Compile / sourceManaged) := (Compile / scalaSource).value

/** Sbt configuration for running Liquibase. */
lazy val LiquibaseConfig = config("liquibase").hide

/** Sbt configuration for running Slickgen. */
lazy val SlickgenConfig = config("slickgen").hide

ivyConfigurations ++= LiquibaseConfig :: SlickgenConfig :: Nil

val jacksonVersion = "2.17.0"
libraryDependencies ++=
  Seq(
    "org.liquibase" % "liquibase-core" % Versions.liquibase,
    "info.picocli" % "picocli" % "4.7.6",
  ).map(_ % LiquibaseConfig.name) ++
  Seq(
    "com.typesafe.slick" %% "slick-codegen" % Versions.slick,
    "org.apache.logging.log4j" % "log4j-slf4j2-impl" % "2.23.1",
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % jacksonVersion,
    "com.fasterxml.jackson.core"       % "jackson-databind"        % jacksonVersion,
  ).map(_ % SlickgenConfig.name) ++
  dbLibraries.flatMap(l => l % LiquibaseConfig.name :: l % SlickgenConfig.name :: Nil)

// Fetch the needed classpaths (JARs).
LiquibaseConfig / managedClasspath := {
  val artifactTypes = (LiquibaseConfig / classpathTypes).value
  Classpaths.managedJars(LiquibaseConfig, artifactTypes, update.value)
}
SlickgenConfig / managedClasspath := {
  val artifactTypes = (SlickgenConfig / classpathTypes).value
  Classpaths.managedJars(SlickgenConfig, artifactTypes, update.value)
}

lazy val slickgen = taskKey[Seq[File]]("Run Slick code generation")
slickgen := {
  val liquibaseCliMainClassName = "liquibase.integration.commandline.LiquibaseCommandLine"
  val liquibaseClasspath        = (LiquibaseConfig / managedClasspath).value.files.map(_.getPath).mkString(":")
  val slickgenCliMainClassName  = "slick.codegen.SourceCodeGenerator"
  val slickgenClasspathFiles    = (SlickgenConfig / managedClasspath).value.files
  val thisDir                   = baseDirectory.value
  val logger                    = streams.value.log

  /** Run the Slick code generation against the given, running database instance.
    *
    * @param url JDBC URL of the database, including credentials
    * @param db the database type
    * @return the generated Scala files
    */
  def runSlickgen(url: String, db: Database): Seq[File] = {
    // First run Liquibase to load the schema

    val liquibaseArgs =
      s"--search-path=$thisDir" :: s"--defaults-file=$thisDir/liquibase.properties" ::
        "update" :: s"--log-file=${target.value}/liquibase.log" :: s"--url=$url" :: Nil

    val options = forkOptions.value.withRunJVMOptions(Vector("--class-path", liquibaseClasspath))
    Fork.java(options, liquibaseCliMainClassName +: liquibaseArgs)
    //TODO Check the exit code

    // Run Slick code generation against the schema

    val packageName  = s"com.performantdata.voucher.database.${db.label}"
    val outputDir    = (Compile / sourceManaged).value
    val file         = outputDir / packageName.replace('.', '/') / "Tables.scala"
    val slickgenArgs = db.slickProfile :: db.jdbcDriver :: url :: outputDir.getPath :: packageName :: Nil

    runner.value.run(slickgenCliMainClassName, slickgenClasspathFiles, slickgenArgs, logger)
      .failed.foreach(t => sys.error(t.getMessage))

    /* Make file writeable so that scalac migration rewriting will work.
     * (The compiler will still issue a warning first, but then do the rewrite.) */
    IO.chmod("rw-r--r--", file)

    Seq(file)
  }

  DatabaseUtilities.runWithDatabase(runSlickgen, logger)
}

Compile / sourceGenerators += slickgen.taskValue
