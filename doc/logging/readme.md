# Logging

Adaptive provides a simple multiplatform logger: [AdaptiveLogger](/adaptive-core/src/commonMain/kotlin/fun/adaptive/log/AdaptiveLogger.kt).

To get a logger use [getLogger](/adaptive-core/src/commonMain/kotlin/fun/adaptive/log/getLogger.kt):

```kotlin
val logger = getLogger("my-logger")
```

Implementations:

- [IOSLogger](/adaptive-core/src/appleMain/kotlin/fun/adaptive/log/IOSLogger.kt) - uses `NSLog`
- [BrowserLogger](/adaptive-core/src/jsMain/kotlin/fun/adaptive/log/BrowserLogger.kt) - uses `console`
- [JvmLogger](/adaptive-core/src/jvmMain/kotlin/fun/adaptive/log/JvmLogger.kt) - uses `SLF4J`

Backend services and workers have their logger defined by their base classes:

```kotlin
class SomeWorker : WorkerImpl<SomeWorker> {

    override suspend fun run() {
        logger.info { "running" }
    }
}
```