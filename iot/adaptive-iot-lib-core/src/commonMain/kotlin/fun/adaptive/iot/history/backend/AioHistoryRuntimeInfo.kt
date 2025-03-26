package `fun`.adaptive.iot.history.backend

import `fun`.adaptive.iot.history.model.AioHistoryMetadata
import `fun`.adaptive.lib.util.temporal.TemporalRecordStore

class AioHistoryRuntimeInfo(
    val metadata: AioHistoryMetadata,
    val store: TemporalRecordStore
)
