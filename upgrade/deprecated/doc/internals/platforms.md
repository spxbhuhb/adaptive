# Platforms

Adaptive needs these functions/classes implemented on a given platform:

**Notes**:

* `clock` has a platform specific implementation now, but maybe it would be better to use `TimeSource.Monotonic` everywhere.
* iOS `logger` is simply println for now, will change when I have time to look up how to do it properly - see #3

Platform specific files and functions:

* [clock](/adaptive-core/src/commonMain/kotlin/fun/adaptive/utility/clock.kt) - `vmNowMicro`, `vmNowSecond`
* [lock](/adaptive-core/src/commonMain/kotlin/fun/adaptive/utility/lock.kt) - `getLock`
* [process](/adaptive-core/src/commonMain/kotlin/fun/adaptive/utility/process.kt) - `exitProcessCommon`
* [random](/adaptive-core/src/commonMain/kotlin/fun/adaptive/utility/random.kt) - `fourRandomInt`
* [logger](/adaptive-core/src/commonMain/kotlin/fun/adaptive/log/getLogger.kt) - `getLogger`

| Platform   | `clock`                  | `lock`            | `process`                                   | `random`                        | `logger`                   |
|------------|--------------------------|-------------------|---------------------------------------------|---------------------------------|----------------------------|
| Browser/JS | `window.performance.now` | empty call        | `window.location.pathname = "/exitProcess"` | `window.crypto.getRandomValues` | `console.*`                |
| iOS*       | `TimeSource.Monotonic`   | `NSRecursiveLock` | `platform.posix.exit`                       | `SecRandomCopyBytes`            | `println`                  |
| JVM        | `System.nanoTime`        | `ReentrantLock`   | `exitProcess`                               | `SecureRandom`                  | `java.util.logging.Logger` |