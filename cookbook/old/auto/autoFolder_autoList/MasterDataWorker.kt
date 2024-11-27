import `fun`.adaptive.auto.api.autoFolder
import `fun`.adaptive.auto.backend.AutoWorker
import `fun`.adaptive.auto.internal.instance.AutoInstance
import `fun`.adaptive.auto.model.AutoConnectionInfo
import `fun`.adaptive.backend.builtin.WorkerImpl
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.setting
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlinx.io.files.Path

/**
 * A worker that creates a permanent, folder-persisted auto list
 * of [MasterDataItem] instances.
 */
class MasterDataWorker(
    path: Path,
    val trace: Boolean = false
) : WorkerImpl<MasterDataWorker> {

    val autoWorker by worker<AutoWorker>()

    val path by setting<Path> { "DATA_PATH" } default path

    val lock = getLock()

    val masterData: AutoInstance<*, *, List<MasterDataItem>, MasterDataItem>
        get() = requireNotNull(masterDataOrNull) { "masterData is null, perhaps the worker is not started" }

    var masterDataOrNull: AutoInstance<*, *, List<MasterDataItem>, MasterDataItem>? = null
        get() = lock.use { field }
        private set(v) {
            lock.use { field = v }
        }

    override suspend fun run() {

        masterDataOrNull = autoFolder<MasterDataItem>(
            autoWorker,
            path,
            { itemId, _ -> "${itemId.peerId}.${itemId.timestamp}.json" },
            defaultWireFormat = MasterDataItem.adatWireFormat,
            trace = trace
        )

    }

    fun connectInfo(): AutoConnectionInfo<List<MasterDataItem>> {
        return masterData.connectInfo()
    }

}