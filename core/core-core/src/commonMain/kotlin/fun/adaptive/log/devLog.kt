package `fun`.adaptive.log

fun devInfo(message : String) = getLogger("dev").info(message)

fun devInfo(message : () -> Any) {
    getLogger("dev").also {
        try {
            it.info(message().toString())
        } catch (ex: Exception) {
            it.error("error while building dev info", ex)
        }
    }
}

fun devInfo(message : String, exception: Exception) = getLogger("dev").fine(message, exception)
