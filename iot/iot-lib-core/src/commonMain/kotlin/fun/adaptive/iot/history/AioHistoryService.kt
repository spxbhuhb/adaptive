package `fun`.adaptive.iot.history

import `fun`.adaptive.adat.encodeToProtoByteArray
import `fun`.adaptive.auth.context.ensureLoggedIn
import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.foundation.query.firstImpl
import `fun`.adaptive.iot.history.backend.AioHistoryWorker
import `fun`.adaptive.iot.history.model.AioBooleanHistoryRecord
import `fun`.adaptive.iot.history.model.AioDoubleHistoryRecord
import `fun`.adaptive.iot.history.model.AioHistoryId
import `fun`.adaptive.iot.history.model.AioHistoryMetadata
import `fun`.adaptive.iot.history.model.AioHistoryQuery
import `fun`.adaptive.iot.point.PointMarkers
import `fun`.adaptive.reflect.typeSignature
import `fun`.adaptive.runtime.GlobalRuntimeContext
import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.builtin.AvBoolean
import `fun`.adaptive.value.builtin.AvConvertedDouble
import `fun`.adaptive.value.builtin.AvDouble
import kotlinx.datetime.Instant

class AioHistoryService : ServiceImpl<AioHistoryService>(), AioHistoryApi {

    companion object {
        lateinit var valueWorker: AvValueWorker
        lateinit var historyWorker: AioHistoryWorker

        fun append(curValue: AvValue2) {
            val safeParentId = checkNotNull(curValue.parentId) { "parentId is null in $curValue" }
            val point = valueWorker.item(safeParentId)
            if (PointMarkers.HIS !in point.markers) return

            when (curValue) {
                is AvConvertedDouble -> historyWorker.append(
                    point.uuid.cast(), curValue.timestamp,
                    AioDoubleHistoryRecord.typeSignature(),
                    AioDoubleHistoryRecord(curValue.timestamp, curValue.originalValue, curValue.convertedValue, curValue.status.flags).encodeToProtoByteArray()
                )

                is AvDouble -> historyWorker.append(
                    point.uuid.cast(), curValue.timestamp,
                    AioDoubleHistoryRecord.typeSignature(),
                    AioDoubleHistoryRecord(curValue.timestamp, curValue.value, curValue.value, curValue.status.flags).encodeToProtoByteArray()
                )

                is AvBoolean -> historyWorker.append(
                    point.uuid.cast(), curValue.timestamp,
                    AioBooleanHistoryRecord.typeSignature(),
                    AioBooleanHistoryRecord(curValue.timestamp, curValue.value, curValue.value, curValue.status.flags).encodeToProtoByteArray()
                )
            }
        }
    }

    override fun mount() {
        check(GlobalRuntimeContext.isServer)
        valueWorker = safeAdapter.firstImpl<AvValueWorker>()
        historyWorker = safeAdapter.firstImpl<AioHistoryWorker>()
    }

    override suspend fun create(uuid: AioHistoryId, signature: String) {
        ensureLoggedIn()
        historyWorker.create(uuid, signature)
    }

    override suspend fun delete(uuid: AioHistoryId) {
        ensureLoggedIn()
        historyWorker.delete(uuid)
    }

    override suspend fun histories(): List<AioHistoryMetadata> {
        ensureLoggedIn()
        return historyWorker.histories()
    }

    override suspend fun append(uuid: AioHistoryId, timestamp: Instant, signature: String, record: ByteArray) {
        ensureLoggedIn()
        historyWorker.append(uuid, timestamp, signature, record)
    }

    override suspend fun query(query: AioHistoryQuery): List<ByteArray> {
        ensureLoggedIn()
        return historyWorker.query(query)
    }

}