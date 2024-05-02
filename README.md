[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.adaptive/adaptive-core)](https://mvnrepository.com/artifact/hu.simplexion.adaptive/adaptive-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Adaptive is a consolidated application development library for Kotlin focusing on:

* expressing our intention,
* clean code, close to no-boilerplate, minimal dependencies,
* frontend and backend components with built-in lifecycle and state handling.

Adaptive comes with a compiler plugin that performs many functions to reach the goals above.

Status: **preview**

While the library already works and provides both client and server side functionality it
is not ready for general production yet. I am working continuously to add improvements.

Adaptive's core is platform antagonistic, pure Kotlin, it should work on whatever KMP is able
to compile for. There are platform specific implementations (like for actual e-mail sending),
but those are in the extension libraries.

Note: As of now JVM and Browser works. It is quite easy to add support for whatever platform, 
see [platforms](./doc/platforms/README.md) for details.

## Examples

### Server side

A very simple e-mail server:

```kotlin
fun main() {
    
    adaptive(AdaptiveServerAdapter()) {

        settings { 
            environment()
            propertyFile("./etc/test.properties")
        }
        
        worker { InMemoryDatabase() }

        store { EmailTable() }
        store { EmailQueue() }
        worker { EmailWorker() }
        service { EmailService() }

    }
    
}
```

### Client side

Shows the current time and increments a counter:

```kotlin
fun main() {
    
    adaptive(AdaptiveDOMAdapter(window.document.body !!)) {

        var counter = 0
        val time = poll(1.seconds, default = Clock.System.now()) { Clock.System.now() }

        div {
            div { text("incremented $counter times(s)") }
            div { text(time.toString()) }
        }

        button("Click to increment!") {
            counter = counter + 1
        }

    }
    
}
```

# Gradle Dependencies

```text
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("hu.simplexion.adaptive") version "2024.05.02"
}

kotlin {
    sourceSets["commonMain"].dependencies {
        implementation("hu.simplexion.adaptive:adaptive-core:2024.05.02")
        implementation("hu.simplexion.adaptive:adaptive-exposed:2024.05.02")
        implementation("hu.simplexion.adaptive:adaptive-email:2024.05.02")
        implementation("hu.simplexion.adaptive:adaptive-browser:2024.05.02")
    }
}
```

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
