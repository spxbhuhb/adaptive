package `fun`.adaptive.lib.util.error

/**
 * Throw a [RequirementException] if [check] returns with false.
 *
 * Useful for providing user feedback.
 *
 * @property   [key]   key to a resource that explains the situation
 */
inline fun requirement(
    key: String,
    vararg arguments: Any,
    check: () -> Boolean
) {
    if (! check()) throw RequirementException(key, arguments.toList())
}