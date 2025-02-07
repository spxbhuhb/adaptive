package `fun`.adaptive.utility

fun <T : Any?> T.println() = this.also { println(it) }

fun Any?.println(formatFun : (String) -> String) = println(formatFun(this.toString()))