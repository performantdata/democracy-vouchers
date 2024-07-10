import java.time.Instant
import scala.sys.process.*

enablePlugins(GitVersioning)

/* Sbt build script, for linking this project to the overall project build.
 *
 * The builds in this subproject use the normal NPM tooling that's typical of front-end development.
 * This script exists to connect that tooling to the root project.
 */
name := "democracy-vouchers-static"
description := "Static Web files for the \"democracy vouchers\" system."

publish / skip := true
publishLocal / skip := true
publishM2 / skip := true

dockerBuild := {
  val cwd = Some(baseDirectory.value)
  val dockerUsernamePrefix = dockerUsername.value.map(_ + "/").getOrElse("")
  val command = Seq("docker", "build",
    "--file=Dockerfile",
    "--build-arg", s"now=${Instant.now}",
    "--build-arg", s"version=${version.value}",
    "--build-arg", s"revision=${git.gitHeadCommit.value.getOrElse("")}",
    "--build-arg", s"organization_name=${organizationName.value}",
    "--build-arg", s"description=${description.value}",
    "--tag", s"$dockerUsernamePrefix${name.value}:latest",
    "--tag", s"$dockerUsernamePrefix${name.value}:${version.value}",
    (target.value / "web" / "public" / "main").toString
  )

  (Process("npm install", cwd) #&&
    Process("npm run build", cwd) #&&
    Process(command, cwd)).!
  //TODO Handle errors.
}
dockerPublish := {
}

//TODO Map the "package" task to something.
