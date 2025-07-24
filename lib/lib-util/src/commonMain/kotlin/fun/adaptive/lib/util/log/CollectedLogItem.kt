package `fun`.adaptive.lib.util.log

import `fun`.adaptive.log.LogLevel
import kotlin.time.Instant

class CollectedLogItem(
    val logger: String? = null,
    val level: LogLevel,
    val time: Instant,
    val message: String,
    val exception: String? = null
)
