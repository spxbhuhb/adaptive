package `fun`.adaptive.lib.util.log

import `fun`.adaptive.log.LogLevel
import kotlinx.datetime.Instant

class CollectedLogItem(
    val logger: String? = null,
    val level: LogLevel,
    val time: Instant,
    val message: String,
    val exception: String? = null
)
