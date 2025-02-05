package `fun`.adaptive.utility

fun Any?.println() = println(this)

fun Any?.println(formatFun : (String) -> String) = println(formatFun(this.toString()))