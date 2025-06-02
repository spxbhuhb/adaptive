package `fun`.adaptive.service.transport

import kotlin.time.Duration

class DelayReconnectException(
    val delay: Duration
) : RuntimeException()