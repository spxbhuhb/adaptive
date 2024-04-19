package hu.simplexion.z2.util

actual fun vmNowMicro(): Long {
    return System.nanoTime() / 1_000
}

actual fun vmNowSecond() : Long {
    return vmNowMicro() / 1_000_000
}