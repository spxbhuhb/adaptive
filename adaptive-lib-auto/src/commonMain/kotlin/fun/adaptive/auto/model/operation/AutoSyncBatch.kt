package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.CollectionBackendBase
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoSyncBatch(
    override val timestamp: LamportTimestamp,
    val addOperations: List<AutoAdd>,
    val modifyOperations: List<AutoModify>
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean) {
        backend as CollectionBackendBase<*>
        addOperations.forEach { it.apply(backend, commit) }
        modifyOperations.forEach { it.apply(backend, commit) }
    }

}