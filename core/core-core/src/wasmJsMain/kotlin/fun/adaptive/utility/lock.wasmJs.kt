package `fun`.adaptive.utility

// FIXME WASM can be multi-threaded, so lock should be an actual lock
actual fun getLock(): Lock =
    BrowserLock()

internal class BrowserLock : Lock {
    override fun lock() {
    }

    override fun unlock() {
    }
}