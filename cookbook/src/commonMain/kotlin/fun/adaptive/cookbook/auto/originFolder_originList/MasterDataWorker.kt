package `fun`.adaptive.cookbook.auto.originFolder_originList

import `fun`.adaptive.auto.api.originFolder
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.utility.waitFor
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path
import kotlin.time.Duration.Companion.seconds

/**
 * A worker that creates a permanent, folder-persisted auto list
 * of [MasterDataItem] instances.
 */
class MasterDataWorker(
    defaultPath: String = "./var/data",
    val trace: Boolean = false
) : WorkerImpl<MasterDataWorker> {

    val autoWorker by worker<AutoWorker>()

    val path by setting<String> { "DATA_PATH" } default defaultPath

    val lock = getLock()

    val masterData: OriginBase<*, *>
        get() = requireNotNull(masterDataOrNull) { "masterData is null, perhaps the worker is not started" }

    var masterDataOrNull: OriginBase<*, *>? = null
        get() = lock.use { field }
        private set(v) {
            lock.use { field = v }
        }

    override suspend fun run() {

        masterDataOrNull = originFolder(
            autoWorker,
            MasterDataItem,
            Json,
            Path(path),
            // This function generates the name of the files.
            // The actual file name is not important, but it should not start with a '.'
            // character as those files are ignored at list load.
            { itemId, _ -> "${itemId.clientId}.${itemId.timestamp}.json" },
            trace = trace,
        )

    }

    fun connectInfo(): AutoConnectInfo {
        return masterData.connectInfo()
    }

}