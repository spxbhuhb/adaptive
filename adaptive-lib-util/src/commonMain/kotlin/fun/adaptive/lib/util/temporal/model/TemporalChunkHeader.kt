package `fun`.adaptive.lib.util.temporal.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.lib.util.temporal.TemporalChunkId
import `fun`.adaptive.lib.util.temporal.TemporalRecordStoreId
import `fun`.adaptive.utility.UUID

@Adat
class TemporalChunkHeader(
    val version: Int = 1,
    val chunkUuid: TemporalChunkId,
    val storeUuid: TemporalRecordStoreId,
    val signature: String
) {
    companion object {
        const val V1 = 1
    }
}