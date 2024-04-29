package hu.simplexion.z2.utility

/**
 * Returns with a number that represents virtual machine time in microseconds.
 * The number has no relation to clock time. The difference between two returned
 * values represents the number of microseconds elapsed between the two calls.
 *
 * On JVM it uses `System.nanoTime`.
 */
expect fun vmNowMicro(): Long

expect fun vmNowSecond() : Long