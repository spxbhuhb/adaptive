# Platforms

Adaptive needs these functions/classes implemented on a given platform:

* [clock](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/utility/clock.kt) - `vmNowMicro`, `vmNowSecond`
* [lock](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/utility/lock.kt) - `Lock`
* [process](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/utility/process.kt) - `exitProcessCommon`
* [random](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/utility/random.kt) - `fourRandomInt`
* [logger](/adaptive-core/src/commonMain/kotlin/hu/simplexion/adaptive/log/logger.kt) - `logger`

| Platform   | `clock`                  | `lock`          | `process`                                   | `random`                        | `logger`                   |
|------------|--------------------------|-----------------|---------------------------------------------|---------------------------------|----------------------------|
| Browser/JS | `window.performance.now` | empty call      | `window.location.pathname = "/exitProcess"` | `window.crypto.getRandomValues` | `console.*`                | 
| JVM        | ` System.nanoTime`       | `ReentrantLock` | `exitProcess`                               | `SecureRandom`                  | `java.util.logging.Logger` |

