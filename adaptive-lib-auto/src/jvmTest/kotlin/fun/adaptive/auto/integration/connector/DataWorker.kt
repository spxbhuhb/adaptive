package `fun`.adaptive.auto.integration.connector

import `fun`.adaptive.auto.api.InstanceBase
import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.frontend.AdatClassFrontend
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.io.files.Path

class DataWorker(
    val trace: Boolean = false
) : WorkerImpl<DataWorker> {

    val autoWorker by worker<AutoWorker>()

    val lock = getLock()

    val masterData: InstanceBase<DataItem>
        get() = requireNotNull(masterDataOrNull) { "masterData is null, perhaps the worker is not started" }

    var masterDataOrNull: InstanceBase<DataItem>? = null
        get() = lock.use { field }
        private set(v) {
            lock.use { field = v }
        }

    override suspend fun run() {
        masterDataOrNull = autoInstance(
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