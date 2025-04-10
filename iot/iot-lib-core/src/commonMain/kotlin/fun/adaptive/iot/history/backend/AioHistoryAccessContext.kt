package `fun`.adaptive.iot.history.backend

import `fun`.adaptive.iot.history.model.AioHistoryId

class AioHistoryAccessContext(
    val uuid: AioHistoryId,
    var runningOperationCount: Int = 0,
    var mergeSemaphore: Boolean = false
)