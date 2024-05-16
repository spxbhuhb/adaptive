[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.adaptive/adaptive-core)](https://mvnrepository.com/artifact/hu.simplexion.adaptive/adaptive-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Adaptive is a consolidated application development library for Kotlin focusing on:

* expressing our intention,
* clean code, close to no-boilerplate, minimal dependencies,
* frontend and backend components with built-in lifecycle and state handling.

Adaptive comes with a compiler plugin that performs many functions to reach the goals above.
 
> [!IMPORTANT]
>
> Project status: **preview**
> 
> While library already works and provides both client and server side functionality,
> it is not even close to be ready for general production.
>
> Please note that this is just a preview release, many basic things you would expect to 
> work are still broken, especially around the code transformation.
> 
> For example, a simple `{ counter++ }` at the right place can result in a compiler plugin
> error.
> 
> I will clean everything up eventually, but for now I consider these low priority issues, 
> ones I can live with. If anyone starts to play around with the project I'll try to
> spend some time on these problems. Feel free to open an issue for any error or even 
> inconvenience you experience.

## Supported platforms

* JVM
* Browser/JS
* Android
* iOS (Arm64, Simulator Arm64, X64)

See [platforms](./doc/platforms/README.md) for details.

## Getting Started

Check out the [adaptive-example](https://github.com/spxbhuhb/adaptive-example) project, or jump to the
* [API](https://github.com/spxbhuhb/adaptive-example/blob/main/shared/src/commonMain/kotlin/hu/simplexion/adaptive/example/api/CounterApi.kt) (defines the API between the clients and the server)
* server
  * [server main](https://github.com/spxbhuhb/adaptive-example/blob/main/server/src/main/kotlin/hu/simplexion/adaptive/example/Application.kt) (server entry point)
  * [service implementation](https://github.com/spxbhuhb/adaptive-example/blob/main/server/src/main/kotlin/hu/simplexion/adaptive/example/service/CounterService.kt) (client request handler, one instance per request)
  * [worker implementation](https://github.com/spxbhuhb/adaptive-example/blob/main/server/src/main/kotlin/hu/simplexion/adaptive/example/worker/CounterWorker.kt) (background worker, one (or few) instances per server)
* client
  * [shared code](https://github.com/spxbhuhb/adaptive-example/blob/main/shared/src/commonMain/kotlin/hu/simplexion/adaptive/example) (shared between all client platforms)
  * [browser main](https://github.com/spxbhuhb/adaptive-example/blob/main/browserApp/src/jsMain/kotlin/main.kt)
  * [android main](https://github.com/spxbhuhb/adative-example/blob/main/androidApp/src/androidMain/kotlin/hu/simplexion/adaptive/example/MainActivity.kt)

## Project Structure

| Component                                                  | Content                                                                                 |
|------------------------------------------------------------|-----------------------------------------------------------------------------------------|
| core                                                       | The fundamental core of the library.                                                    |
| &nbsp;&nbsp;[adat](doc/adat/README.md)                     | Data classes with many convenience functions, metadata and serialization support.       |
| &nbsp;&nbsp;[foundation](doc/foundation/README.md)         | Fundamental classes and interfaces for building adaptive structures.                    |
| &nbsp;&nbsp;[server](doc/server/README.md)                 | Server side adaptive fragments: workers, services, stores, settings.                    |
| &nbsp;&nbsp;[service](doc/service/README.md)               | Client-server communication with simple function calls.                                 |
| &nbsp;&nbsp;[wireformat](doc/wireformat/README.md)         | Serialization (protobuf and Json).                                                      |
| gradle-plugin                                              | The Gradle plugin.                                                                      |
| kotlin-plugin                                              | The Kotlin compiler plugin.                                                             |
| lib                                                        | Application level libraries such as UI, E-mail, etc.                                    |
| &nbsp;&nbsp;[email](adaptive-lib/adaptive-lib-email)       | Email worker (JavaMail), tables (Exposed) and service to send emails.                   |
| &nbsp;&nbsp;[exposed](adaptive-lib/adaptive-lib-exposed)   | Integration with Exposed, HikariPool worker.                                            |
| &nbsp;&nbsp;[ktor](adaptive-lib/adaptive-lib-ktor)         | Ktor Worker with websockets and static directory serving. Transport for services.       |
| sandbox                                                    | Project used during development to try things our without booting up the whole example. |
| &nbsp;&nbsp;[lib](adaptive-sandbox/adaptive-sandbox-lib)   | Library sandbox.                                                                        |
| &nbsp;&nbsp;[app](adaptive-sandbox/adaptive-sandbox-app)   | Application sandbox.                                                                    |
| ui                                                         | User interface modules.                                                                 |
| &nbsp;&nbsp;[common](adaptive-ui/adaptive-ui-common)       | Basic UI fragments for the supported platforms.                                         |

## Gradle Dependencies

**Note**: The snapshot version is not on Maven Central. I'll update this when an actual preview is released.

```text
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("hu.simplexion.adaptive") version "2024.05.07-SNAPSHOT"
}

kotlin {
    sourceSets["commonMain"].dependencies {
        implementation("hu.simplexion.adaptive:adaptive-core:2024.05.07-SNAPSHOT")
        implementation("hu.simplexion.adaptive:adaptive-ktor:2024.05.07-SNAPSHOT")        
        implementation("hu.simplexion.adaptive:adaptive-exposed:2024.05.07-SNAPSHOT")
        implementation("hu.simplexion.adaptive:adaptive-email:2024.05.07-SNAPSHOT")
        implementation("hu.simplexion.adaptive:adaptive-ui:2024.05.07-SNAPSHOT")
    }
}
```

## Building Adaptive

Run the `build` task of the root project.

> [!Note]
>
> If you are building with iOS targets you have to run the build on Mac OS X. This is a requirement from Apple,
> see [Multiplatform Setup](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-setup.html) for details.
>
> To disable iOS targets set the version of `ios-support` to `none` in [libs.versions.toml](gradle/libs.versions.toml). Any
> value other than `none` adds support for iOS.
>
> This is also useful when you don't want to wait for the iOS linking during development.

Adaptive uses a composite Gradle build. These tasks are defined for the root project and run the appropriate task for
each included project:

- `build`
- `clean`
- `publishToMavenLocal`
- `kotlinUpgradeYarnLock`

Everything uses the dependencies defined in [libs.versions.toml](gradle/libs.versions.toml).

To bump Adaptive version number:

- change `adaptive` version in  [libs.versions.toml](gradle/libs.versions.toml)
- change `PLUGIN_VERSION` in [AdaptiveGradlePlugin](adaptive-gradle-plugin/src/main/kotlin/hu/simplexion/adaptive/gradle/AdaptiveGradlePlugin.kt) (also see https://github.com/spxbhuhb/adaptive/issues/7)

## Debugging Adaptive

To see what the plugin does, add this to `build.gradle.kts`. When `pluginDebug` is true, the plugin creates
files for each compilation in the `pluginLogDir` directory.

```kotlin
adaptive {
    pluginDebug = true
    pluginLogDir = projectDir.toPath()
}
```

## Credits

**Multiplatform**

* [Kotlin](https://kotlinlang.org) (by the Kotlin Foundation, Apache 2.0)
* [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) (by JetBrains, Apache 2.0)
* [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) (?, Apache 2.0)

**Server side**

* [PostgreSQL](https://www.postgresql.org) (by The PostgreSQL Global Development Group, PostgreSQL Licence)
* [Exposed](https://github.com/JetBrains/Exposed) (by the JB Team, Apache 2.0)
* [HikariCP](https://github.com/brettwooldridge/HikariCP) (by Brett Wooldridge, Apache 2.0)
* [LOGBack](http://logback.qos.ch) (by QOS.ch, EPL v1.0 or LGPL 2.1)
* [JavaMail](https://javaee.github.io/javamail/)  (by Oracle, CDDL 1.0)

**Building**

* [Gradle](https://gradle.org) (by Gradle Inc., Apache 2.0)

**Testing**

* [H2](https://www.h2database.com/) (by multiple contributors, MPL 2.0 or EPL 1.0)
* [SubEtha SMTP](https://github.com/voodoodyne/subethasmtp) (by SubEthaMail.org, Apache 2.0)

**Inspiration**

* [Svelte](https://svelte.dev) (the whole idea)
* [KVision](https://kvision.io) (some service related ideas)
* [Tailwindcss](https://tailwindcss.com) (style concept)

## License

> Copyright (c) 2024 Simplexion Kft, Hungary and contributors
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this work except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
