import sbt.*

object TasksPlugin extends AutoPlugin {
  object autoImport {
    // tasks that are special to our build
    val dockerBuild = taskKey[Unit]("Builds Docker images and loads them to the local store.")
    val dockerPublish = taskKey[Unit]("Publishes Docker images to the repository.")
  }
}
