package hu.simplexion.adaptive.utility

open class CleanupHandler<T>(
    val cleanupFun: (it: T) -> Unit
)
