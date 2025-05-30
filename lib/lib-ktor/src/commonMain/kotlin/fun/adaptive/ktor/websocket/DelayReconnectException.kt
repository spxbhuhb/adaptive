package `fun`.adaptive.ktor.websocket

import kotlin.time.Duration

class DelayReconnectException(
    val delay: Duration
) : RuntimeException()