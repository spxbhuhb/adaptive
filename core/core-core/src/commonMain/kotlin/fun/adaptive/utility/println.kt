package `fun`.adaptive.utility

@Deprecated("Use debug only during development, remove all calls before commit.")
fun <T : Any?> T.debug() = this.also { println(it) }

@Deprecated("Use debug only during development, remove all calls before commit.")
fun <T : Any?> T.debug(formatFun: (String) -> String): T = this.also { println(formatFun(this.toString())) }

@Deprecated("Use debug only during development, remove all calls before commit.")
fun debug(vararg items: Any?) {
    println(items.joinToString(","))
}

/**
 * Prints out [this] to standard output and returns with [this].
 *
 * Use this function when you write a development/troubleshooting tool
 * that dumps some content on standard output.
 */
fun <T : Any?> T.dump() = this.also { println(it) }

/**
 * Prints out [this] to standard output and returns with [this].
 *
 * Use this function when you write a development/troubleshooting tool
 * that dumps some content on standard output.
 */
inline fun <reified T> dump(value : () -> T) = value().also { println(it) }