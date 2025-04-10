package `fun`.adaptive.utility

fun <T : Any?> T.debug() = this.also { println(it) }

fun <T : Any?> T.debug(formatFun: (String) -> String): T = this.also { println(formatFun(this.toString())) }

fun debug(vararg items: Any?) {
    println(items.joinToString(","))
}