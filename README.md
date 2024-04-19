[![Maven Central](https://img.shields.io/maven-central/v/hu.simplexion.z2/z2-core)](https://mvnrepository.com/artifact/hu.simplexion.z2/z2-core)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

KMP full-stack app development library.

Status: **migrating the code from the old proof-of-concept project**

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