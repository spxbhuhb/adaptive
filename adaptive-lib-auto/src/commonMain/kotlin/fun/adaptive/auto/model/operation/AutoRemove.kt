package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.ItemId
import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.backend.BackendBase
import `fun`.adaptive.auto.backend.CollectionBackendBase

@Adat
class AutoRemove(
    override val timestamp: LamportTimestamp,
    val itemIds: Set<ItemId>
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean, distribute: Boolean) {
        backend as CollectionBackendBase
        backend.remove(this, commit, distribute)
    }

}