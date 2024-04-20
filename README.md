[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.z2/z2-core)](https://mvnrepository.com/artifact/hu.simplexion.z2/z2-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Full-stack application development library, focusing on:

* intentional coding (just write down what you want in a short, concise way)
* clean code
* no-boilerplate

Z2 comes with a compiler plugin that performs many functions reach the goals above.

Status: **migrating the code from the old proof-of-concept project**

# Modules

- [Adaptive](doc/adaptive/README.md) reactive UI (multiplatform, React and Compose independent, Svelte-like)
- [Services](doc/services/README.md) transparent client-server communication with simple function calls
- [Schematic] data model definition
- [WireFormat](doc/wireformat/README.md) lightweight serialization (JSON, Protobuf) optimized for RPC

# Artifacts

The modules are spread over the three artifacts the library provides:

* [Core] fundamental code necessary for the compiler plugin to work with
* [Plugin] the compiler plugin that performs all the code transformations
* [Lib] application level code that uses Core and Plugin

# Building Z2

Run the `build` task of the root project.

Building the project publishes the Kotlin compiler plugin into the local Maven repository. This is necessary as `z2-lib`
needs the Kotlin plugin to compile. To publish into Maven local you have to set up the signing as documented in
[The Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html). Long story short, put the
following info into your `gradle.properties`:

```properties
signing.gnupg.executable=/usr/local/bin/gpg
signing.gnupg.keyName=<your-key-short-name>
```

Technically you don't need the executable but for me, it was necessary, I have no idea why.

# Bits

The colors are created with the [Material Theme Builder](https://material-foundation.github.io/material-theme-builder/)

## License

> Copyright (c) 2023 Simplexion Kft, Hungary and contributors
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