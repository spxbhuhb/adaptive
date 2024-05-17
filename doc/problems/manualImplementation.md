# Problems with manualImplementation

A normal fragment looks like this:

```kotlin
@Adaptive
fun someFun(name : String) {
    text("Hello $name!")
    /* other stuff ... */
}
```

A manual implementation usually looks like one of these:

```kotlin
@Adaptive
fun someFun(name : String) {
    manualImplementation(name)
}

@AdaptiveExpect
fun someFunExpect(name : String) {
    manualImplementation(name)
}
```

Possible causes:

* the plugin is not applied to the project:
  * if you use gradle catalog: add `alias(libs.plugins.adaptive)` to `plugins` in `build.gradle.kts`
  * otherwise: add `id("hu.simplexion.adaptive")` to `plugins` in `build.gradle.kts`
* the manual implementation is actually missing
  * if it's your code, well, write it :P
  * otherwise, open a GitHub issue or contact me on Slack