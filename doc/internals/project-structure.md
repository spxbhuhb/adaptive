# Project Structure

| Component                                                      | Content                                                                           |
|----------------------------------------------------------------|-----------------------------------------------------------------------------------|
| core                                                           | The fundamental core of the library.                                              |
| &nbsp;&nbsp;[adat](../adat)                                    | Data classes with many convenience functions, metadata and serialization support. |
| &nbsp;&nbsp;[foundation](../foundation)                        | Fundamental classes and interfaces for building adaptive structures.              |
| &nbsp;&nbsp;[server](../server)                                | Server side adaptive fragments: workers, services, stores, settings.              |
| &nbsp;&nbsp;service                                            | Client-server communication with simple function calls.                           |
| &nbsp;&nbsp;wireformat                                         | Serialization (protobuf and Json).                                                |
| gradle-plugin                                                  | The Gradle plugin.                                                                |
| kotlin-plugin                                                  | The Kotlin compiler plugin.                                                       |
| lib                                                            | Application level libraries such as UI, E-mail, etc.                              |
| &nbsp;&nbsp;[email](../../adaptive-lib/adaptive-lib-email)     | Email worker (JavaMail), tables (Exposed) and service to send emails.             |
| &nbsp;&nbsp;[exposed](../../adaptive-lib/adaptive-lib-exposed) | Integration with Exposed, HikariPool worker.                                      |
| &nbsp;&nbsp;[ktor](../../adaptive-lib/adaptive-lib-ktor)       | Ktor Worker with websockets and static directory serving. Transport for services. |
| &nbsp;&nbsp;[lib](../../adaptive-lib/adaptive-lib-sandbox)     | Library sandbox.                                                                  |
| sandbox                                                        | Sandbox project to try things our without booting up the whole example.           |
| site                                                           | Source of [adaptive.fun](https://adaptive.fun)                                    |
| ui                                                             | User interface modules.                                                           |
| &nbsp;&nbsp;[common](../../adaptive-ui/adaptive-ui-common)     | Basic UI fragments for the supported platforms.                                   |
