package `fun`.adaptive.utility

fun <T : Any?> T.debug() = this.also { println(it) }

fun Any?.debug(formatFun: (String) -> String) = println(formatFun(this.toString()))

fun debug(vararg items: Any?) {
    println(items.joinToString(","))
}