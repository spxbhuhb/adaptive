package `fun`.adaptive.cookbook.auto.originListPoly_originFolderPoly_originListPoly

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.api.originFolderPoly
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.origin.OriginBase
import `fun`.adaptive.auto.model.AutoConnectInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import `fun`.adaptive.wireformat.api.Json
import kotlinx.io.files.Path

/**
 * A worker that creates a permanent, folder-persisted auto list
 * of polymorphic instances.
 */
class MasterDataWorker(
    path: String,
    val trace: Boolean = false
) : WorkerImpl<MasterDataWorker> {

    val autoWorker by worker<AutoWorker>()

    val path by setting<String> { "DATA_PATH" } default path

    val lock = getLock()

    val masterData: OriginBase<*, *, List<AdatClass>>
        get() = requireNotNull(masterDataOrNull) { "masterData is null, perhaps the worker is not started" }

    var masterDataOrNull: OriginBase<*, *, List<AdatClass>>? = null
        get() = lock.use { field }
        private set(v) {
            lock.use { field = v }
        }

    override suspend fun run() {

        masterDataOrNull = originFolderPoly(
            autoWorker,
            StringItem,
            Json,
            Path(path),
            // This function generates the name of the files.
            // The actual file name is not important, but it should not start with a '.'
            // character as those files are ignored at list load.
            { itemId, _ -> "${itemId.peerId}.${itemId.timestamp}.json" },
            trace = trace,
        )

    }

    fun connectInfo(): AutoConnectInfo<List<AdatClass>> {
        return masterData.connectInfo()
    }

}