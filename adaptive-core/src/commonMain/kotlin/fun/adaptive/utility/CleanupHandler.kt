package `fun`.adaptive.utility

open class CleanupHandler<T>(
    val cleanupFun: (it: T) -> Unit
)
