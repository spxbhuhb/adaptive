package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.CollectionBackendBase
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoSyncEnd(
    override val timestamp: LamportTimestamp
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean) {
        backend as CollectionBackendBase<*>
        backend.syncEnd(this, commit)
    }

}