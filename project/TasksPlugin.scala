import sbt.*

/** Defines some tasks that are special to our build. */
object TasksPlugin extends AutoPlugin {
  object autoImport {
    /* Typical Docker plugins reuse the standard `publishLocal` and `publish` tasks.  This isn't really correct, since
     * those are documented as being for Ivy artifacts, not Docker images.
     */
    val dockerBuild = taskKey[Unit]("Builds Docker images and loads them to the local store.")
    val dockerPublish = taskKey[Unit]("Publishes Docker images to the repository.")
  }
}
