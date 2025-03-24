package `fun`.adaptive.iot.history

import `fun`.adaptive.iot.history.model.AioHistoryId
import `fun`.adaptive.iot.history.model.AioHistoryMetadata
import `fun`.adaptive.iot.history.model.AioHistoryQuery
import `fun`.adaptive.service.ServiceApi
import kotlinx.datetime.Instant

@ServiceApi
interface AioHistoryApi {

    suspend fun create(uuid: AioHistoryId, signature: String)

    suspend fun delete(uuid: AioHistoryId)

    suspend fun histories(): List<AioHistoryMetadata>

    suspend fun append(uuid: AioHistoryId, timestamp: Instant, signature: String, record: ByteArray)

    suspend fun query(query: AioHistoryQuery): List<ByteArray>

}