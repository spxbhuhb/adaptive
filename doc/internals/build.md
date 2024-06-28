# Build

1. Add `local.propeties` file to
    1. `adaptive-core`
    2. `adaptive-ui/adaptive-ui-common`
    3. `adaptive-sandbox`
2. Run the `build` task of the root project.

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
- change `PLUGIN_VERSION` in [AdaptiveGradlePlugin](adaptive-gradle-plugin/src/main/kotlin/hu/simplexion/adaptive/gradle/AdaptiveGradlePlugin.kt) (also
  see https://github.com/spxbhuhb/adaptive/issues/7)

## Debug

To see what the plugin does, add this to `build.gradle.kts`. When `pluginDebug` is true, the plugin creates
debug files for each compilation in the `build/adaptive/debug` directory.

**CAREFUL** These files are BIG, don't forget to switch off this option!

```kotlin
adaptive {
    pluginDebug = true
}
```