package `fun`.adaptive.foundation.api

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.LocalContext
import kotlin.reflect.KClass

/**
 * Try to find a local context of type `T` and call the block with it.
 *
 * The function goes up in the fragment tree from [start] and checks if there
 * is a context between the parents with the requested context type. If so,
 * calls block with the context.
 *
 * If there is no such parent, calls the block with `null`.
 *
 * @param  start  A fragment which is inside the context, the starting point for the search.
 *
 */
inline fun <reified T : Any> withLocalContext(start: AdaptiveFragment, block: (T?) -> Unit) =
    block(start.findContext(T::class))

/**
 * Find the first [LocalContext] fragment between parents of [this] with context
 * of type [T] and return with the context.
 *
 * @return  the context or null if there is no such fragment
 */
inline fun <reified T : Any> AdaptiveFragment.findContext() =
    findContext(T::class)

/**
 * Find the first [LocalContext] fragment between parents of [this] with context
 * of [type] and return with the context.
 *
 * @return  the context or null if there is no such fragment
 */
fun <T : Any> AdaptiveFragment.findContext(type: KClass<T>): T? {
    var current: AdaptiveFragment? = this

    while (current != null) {
        @Suppress("UNCHECKED_CAST")
        if (current is LocalContext && type.isInstance(current.context)) return current.context as T
        current = current.parent
    }

    return null
}