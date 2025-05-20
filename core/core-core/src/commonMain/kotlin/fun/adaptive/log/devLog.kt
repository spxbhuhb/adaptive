package `fun`.adaptive.log

fun devInfo(message : String) = getLogger("dev").info(message)

fun devInfo(message : () -> Any) = getLogger("dev").info(message().toString())

fun devInfo(message : String, exception: Exception) = getLogger("dev").fine(message, exception)
