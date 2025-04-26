package `fun`.adaptive.log

fun interface LoggerFactory {
    fun getLogger(name : String) : AdaptiveLogger
}