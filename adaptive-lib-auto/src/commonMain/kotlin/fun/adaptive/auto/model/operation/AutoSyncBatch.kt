package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoSyncBatch(
    override val timestamp: LamportTimestamp,
    val addOperations: List<AutoAdd>,
    val modifyOperations: List<AutoModify>
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean) {
        backend as AutoCollectionBackend<*>
        addOperations.forEach { it.apply(backend, commit) }
        modifyOperations.forEach { it.apply(backend, commit) }
    }

}