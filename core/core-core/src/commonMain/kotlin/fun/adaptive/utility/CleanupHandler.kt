package `fun`.adaptive.utility

fun interface CleanupHandler<T> {
    operator fun invoke(it: T)
}
