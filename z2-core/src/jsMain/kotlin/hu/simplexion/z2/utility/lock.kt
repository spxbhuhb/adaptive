package hu.simplexion.z2.utility

/**
 * This is a copy of Ktor multiplatform lock as we clearly need it.
 * Only JVM and JS implementations are added at the moment, I'll add native
 * when it becomes important:
 *
 * https://github.com/ktorio/ktor/blob/master/ktor-utils/posix/src/io/ktor/util/LockNative.kt
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class Lock actual constructor() {
    actual fun lock() {
    }

    actual fun unlock() {
    }
}