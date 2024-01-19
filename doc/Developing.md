# Developing this project
## publishing the application

The `sbt Docker/publish` command compiles the code, packages the application, and publishes it to Docker Hub.
Obviously this will only work if you have the proper credentials to publish to my username;
so to publish the image only to your local Docker environment, run
```shell
sbt Docker/publishLocal
```
instead.

## running the application

```shell
docker run -p 127.0.0.1:9000:9000/tcp performantdata/democracy-vouchers
```
The Swagger UI will be at http://localhost:9000/docs/.

## development tools

The application's development uses the following:

- [tapir][tapir] (a.k.a. tAPIr), for describing the HTTP API of the server.
  Tapir converts the description into [OpenAPI],
  and includes the Swagger UI for interpreting the OpenAPI as a Web UI.

- [http4s][http4s], to implement the HTTP service
- [Caliban][Caliban], to implement a GraphQL service on one of the HTTP endpoints
- [ip4s][ip4s], to represent IP address data in Scala
- logging
  - [Log4j2][Log4j2], for the logging implementation

- [sbt-native-packager][sbt-native-packager], for producing the Docker image.
  This provides the shell scripts that launch the JVM application,
  and runs the `jlink` tool that packages only the _required_ JVM modules.

- [Debian GNU/Linux][Debian], as the base OS on which the application runs.
  I use a "slim" version, which is about 75MB.
  While Nix may be a better choice for a minimal image, I wanted some environment for debugging issues.

[Caliban]: https://ghostdogpr.github.io/caliban/
[Debian]: https://debian.org/
[http4s]: https://http4s.org/
[ip4s]: https://github.com/Comcast/ip4s
[Log4j2]: https://logging.apache.org/log4j/2.x/
[OpenAPI]: https://swagger.io/specification/
[sbt-native-packager]: https://sbt-native-packager.readthedocs.io/en/latest/
[tapir]: https://tapir.softwaremill.com/en/latest/
