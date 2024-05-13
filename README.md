[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.adaptive/adaptive-core)](https://mvnrepository.com/artifact/hu.simplexion.adaptive/adaptive-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Adaptive is a consolidated application development library for Kotlin focusing on:

* expressing our intention,
* clean code, close to no-boilerplate, minimal dependencies,
* frontend and backend components with built-in lifecycle and state handling.

Adaptive comes with a compiler plugin that performs many functions to reach the goals above.

Status: **initial development**

While the library already works and provides both client and server side functionality it
is not ready for general production yet. I am working continuously to add improvements.

Supported platforms:

* JVM
* Browser/JS
* Android
* iOS Arm64
* iOS Simulator Arm64
* iOS X64

See [platforms](./doc/platforms/README.md) for details.

## Basic Concept

Adaptive has been inspired by [Svelte](https://svelte.dev) and it basically serves the same purpose as other 
reactive frameworks such as Compose or React. However, there are a few key differences:

* also supports building of servers, not just user interfaces
* all variables are reactive by default (there is no `remember`)
* there is no recomposition/re-rendering, instead fragments are "patched"

Here is a very simple server and a very simple client:

```kotlin
fun main() {
    
    adaptive(AdaptiveServerAdapter<Any>(true)) {
    
        settings { propertyFile { "./etc/example.properties "} }

        service { CounterService() }
        worker { CounterWorker() }

        worker { KtorWorker() }

    }
    
}
```

```kotlin
defaultServiceCallTransport = BasicWebSocketServiceCallTransport()
val counterService = getService<CounterApi>()

fun main() {
    adaptive(AdaptiveDOMAdapter()) {
        val time = poll(1.seconds, now()) { now() }
        counterWithTime(time)
    }
}

@Adaptive
fun counterWithTime(time : Instant) {
    val counter = poll(1.seconds, 0) { counterService.incrementAndGet() }
    text("$time $counter")
}
```

# Gradle Dependencies

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

## Project Structure

| Component                                             | Content                                                                                 |
|-------------------------------------------------------|-----------------------------------------------------------------------------------------|
| core                                                  | The fundamental core of the library.                                                    |
| &nbsp;&nbsp;[adat](doc/adat/README.md)                | Data classes with many convenience functions, metadata and serialization support.       |
| &nbsp;&nbsp;[base](doc/base/README.md)                | Base definitions for defining adaptive structures.                                      |
| &nbsp;&nbsp;[server](doc/server/README.md)            | Server side adaptive fragments: workers, services, stores, settings.                    |
| &nbsp;&nbsp;[service](doc/service/README.md)          | Client-server communication with simple function calls.                                 |
| &nbsp;&nbsp;[wireformat](doc/wireformat/README.md)    | Serialization (protobuf and Json).                                                      |
| example                                               | A complete example project. Its Gradle setup is independent from the main project.      |
| &nbsp;&nbsp;[androidApp](adaptive-example/androidApp) | Example application for Android.                                                        |
| &nbsp;&nbsp;[browserApp](adaptive-example/browserApp) | Example application for web browsers (JS).                                              |
| &nbsp;&nbsp;[iosApp](adaptive-example/iosApp)         | Example application for iOS devices.                                                    |
| &nbsp;&nbsp;[server](adaptive-example/server)         | Example server application.                                                             |
| &nbsp;&nbsp;[shared](adaptive-example/shared)         | Code shared between the example applications.                                           |
| gradle-plugin                                         | The Gradle plugin.                                                                      |
| kotlin-plugin                                         | The Kotlin compiler plugin.                                                             |
| lib                                                   | Application level libraries such as UI, E-mail, etc.                                    |
| &nbsp;&nbsp;[email](adaptive-lib/adaptive-email)      | Email worker (JavaMail), tables (Exposed) and service to send emails.                   |
| &nbsp;&nbsp;[exposed](adaptive-lib/adaptive-exposed)  | Integration with Exposed, HikariPool worker.                                            |
| &nbsp;&nbsp;[ktor](adaptive-lib/adaptive-ktor)        | Ktor Worker with websockets and static directory serving. Transport for services.       |
| &nbsp;&nbsp;[email](adaptive-lib/adaptive-template)   | Build adaptive structures from templates (no-code/low-code).                            |
| &nbsp;&nbsp;[email](adaptive-lib/adaptive-ui)         | Basic UI fragments for the supported platforms.                                         |
| sandbox                                               | Project used during development to try things our without booting up the whole example. |

## Building Adaptive

Run the `build` task of the root project.

Building the project publishes the Kotlin compiler plugin into the local Maven repository. This is necessary as `adaptive-lib`
needs the Kotlin plugin to compile. To publish into Maven local you have to set up the signing as documented in
[The Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html). Long story short, put the
following info into your `gradle.properties`:

```properties
signing.gnupg.executable=/usr/local/bin/gpg
signing.gnupg.keyName=<your-key-short-name>
```

Theoretically you don't need the executable, but for me, it was necessary, I have no idea why.

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
