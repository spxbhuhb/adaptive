package `fun`.adaptive.log

fun devInfo(message : String) = getLogger("dev").info(message)

fun devInfo(message : () -> String) = getLogger("dev").info(message)

fun devInfo(message : String, exception: Exception) = getLogger("dev").fine(message, exception)
