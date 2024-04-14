import sbt.internal.util.ManagedLogger

import java.io.EOFException
import scala.util.chaining.scalaUtilChainingOps
import sys.process.*

/** Description of a type of database server to use with the Slick code generation. */
trait Database {
  /** A label, unique to this database type, to be used for naming JARs, etc. */
  val label: String

  /** The subprotocol string used for this database type in a JDBC URL. */
  def jdbcSubprotocol: String = label

  /** Name of the Docker image, at Docker Hub, for the database. */
  val image: String

  /** User name of the database administrator built into the Docker image. */
  val adminUser: String

  /** TCP port on which the database server listens inside its container. */
  val port: Int

  /** Name of the environment variable to set with the initial administrator password. */
  val passwordEnvVar: String
}

/** The latest version of the PostgreSQL database server. */
case object PostgreSQL extends Database {
  override val label          = "postgresql"
  override val image          = "postgres:16.2"
  override val adminUser      = "postgres"
  override val port           = 5432
  override val passwordEnvVar = "POSTGRES_PASSWORD"
}

/** Utilities for running a Docker container for a database service. */
object DatabaseUtilities {
  private[this] val database = PostgreSQL
  private[this] val password = "mysecretpassword"

  /** Call the given function, providing it with a database service.
    *
    * @param f
    *   function taking as its parameters:
    *   1. the JDBC URL of the database
    *   1. the database type
    *   1. the administrator user's password
    */
  def runWithDatabase(f: (String, Database, String) => Unit, logger: ManagedLogger): Unit = {
    val startContainerCommand =
      s"docker container run --detach --rm --publish-all --env ${database.passwordEnvVar}=$password ${database.image}"
    val containerId = runCommand(startContainerCommand, logger)
    Thread.sleep(5000L)  //TODO Replace with port readiness test.

    val getPortCommand       = s"docker container port $containerId ${database.port}/tcp"
    val stopContainerCommand = s"docker container stop $containerId"

    try {
      val authority = runCommand(getPortCommand, logger)
        .replace("0.0.0.0:", "127.0.0.1:")
        .replace("[::]:", "[::1]:")

      val url = s"jdbc:${database.jdbcSubprotocol}://$authority/"
      f(url, database, password)
    }
    finally
      () //runCommand(stopContainerCommand, logger)
  }

  /** Return the first line of standard output from the given command.
    *
    * Logs standard error output to the given logger at WARN level.
    * @throws RuntimeException if the command returns a non-zero exit code
    * @throws EOFException if the command had no output
    */
  private[this] def runCommand(command: String, logger: ManagedLogger): String = {
    var firstLine = Option.empty[String]
    val process = command run ProcessLogger(line => if (firstLine.isEmpty) firstLine = Some(line), logger.warn(_))
    val exitCode = process.exitValue()
    if (exitCode != 0) {
      val msg = s"""Command "$command" returned code $exitCode."""
      logger.warn(msg)
      throw new RuntimeException(msg)
    }
    else
      firstLine.fold {
        val msg = s"""No output from command "$command"."""
        logger.warn(msg)
        throw new EOFException(msg)
      }{
        _.tap(line => logger.debug(s"""Command "$command" returned first line "$line"."""))
      }
  }
}
