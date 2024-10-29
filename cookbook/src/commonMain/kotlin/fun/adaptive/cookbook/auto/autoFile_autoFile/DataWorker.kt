package `fun`.adaptive.cookbook.auto.autoFile_autoFile

import `fun`.adaptive.auto.api.autoFile
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

/**
 * A worker that creates a permanent, file-persisted auto instance of [DataItem].
 */
class DataWorker(
    path: Path,
    val trace: Boolean = false
) : WorkerImpl<DataWorker> {

    val autoWorker by worker<AutoWorker>()

    val path by setting<Path> { "DATA_PATH" } default path

    val lock = getLock()

    val masterData: AutoInstance<*, *, DataItem, DataItem>
        get() = requireNotNull(masterDataOrNull) { "masterData is null, perhaps the worker is not started" }

    var masterDataOrNull: AutoInstance<*, *, DataItem, DataItem>? = null
        get() = lock.use { field }
        private set(v) {
            lock.use { field = v }
        }

    override suspend fun run() {

        masterDataOrNull = autoFile(
            autoWorker,
            DataItem,
            path,
            DataItem(),
            Json,
            trace = trace
        )

    }

    fun connectInfo(): AutoConnectionInfo<DataItem> {
        return masterData.connectInfo()
    }

}