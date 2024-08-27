# Publishing

## Prerequisites

To publish into a Maven repository you have to set up the signing as documented in [The Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html).

Long story short, put the following info into your `gradle.properties`:

```properties
signing.gnupg.executable=/usr/local/bin/gpg
signing.gnupg.keyName=<your-key-short-name>
```

Theoretically you don't need the executable, but for me, it was necessary, I have no idea why.

The build requires the Android 34 SDK installed.
Path to the SDK has to be set in the [adaptive-ui/adaptive-ui-common/local.properties](/adaptive-ui/local.properties) file.
For me, it is like this:

```text
sdk.dir=/Users/<your-username>/Library/Android/sdk
```

## Build And Publish

```text
./gradlew clean
./gradlew build
./gradlew publish
```

## Release

[Maven Central](https://central.sonatype.com)
