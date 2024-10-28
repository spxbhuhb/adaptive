package `fun`.adaptive.auto.integration.connector

import `fun`.adaptive.auto.api.ItemBase
import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use

class DataWorker(
    val trace: Boolean = false
) : WorkerImpl<DataWorker> {

    val autoWorker by worker<AutoWorker>()

    val lock = getLock()

    val masterData: ItemBase<DataItem>
        get() = requireNotNull(masterDataOrNull) { "masterData is null, perhaps the worker is not started" }

    var masterDataOrNull: ItemBase<DataItem>? = null
        get() = lock.use { field }
        private set(v) {
            lock.use { field = v }
        }

    override suspend fun run() {
        masterDataOrNull = autoItem(
            autoWorker,
            DataItem,
            DataItem(),
            trace = trace
        )
    }

    fun connectInfo(): AutoConnectionInfo<DataItem> =
        lock.use {
            return masterData.connectInfo()
        }

}