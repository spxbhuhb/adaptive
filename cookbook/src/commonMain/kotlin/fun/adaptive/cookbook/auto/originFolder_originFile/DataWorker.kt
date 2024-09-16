package `fun`.adaptive.cookbook.auto.originFolder_originFile

import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.frontend.FolderFrontend
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

/**
 * A worker that creates a permanent, folder-persisted auto list
 * of [DataItem] instances.
 */
class DataWorker(
    path: Path,
    val trace: Boolean = false
) : WorkerImpl<DataWorker> {

    val autoWorker by worker<AutoWorker>()

    val path by setting<Path> { "DATA_PATH" } default path

    val lock = getLock()

    val masterData: OriginBase<*, FolderFrontend<DataItem>, List<DataItem>>
        get() = requireNotNull(masterDataOrNull) { "masterData is null, perhaps the worker is not started" }

    var masterDataOrNull: OriginBase<*, FolderFrontend<DataItem>, List<DataItem>>? = null
        get() = lock.use { field }
        private set(v) {
            lock.use { field = v }
        }

    override suspend fun run() {

        masterDataOrNull = autoFolder(
            autoWorker,
            DataItem,
            Json,
            path,
            // This function generates the name of the files.
            // The actual file name is not important, but it should not start with a '.'
            // character as those files are ignored at list load.
            { itemId, _ -> "${itemId.peerId}.${itemId.timestamp}.json" },
            trace = trace,
        )

        masterData.frontend.add(DataItem(UUID(), "record-name-server"))
    }

    fun connectInfo(filter : String): AutoConnectInfo<DataItem> =
        lock.use {
            requireNotNull(masterData.connectInfo<DataItem> { filter in it.recordName })
        }

}