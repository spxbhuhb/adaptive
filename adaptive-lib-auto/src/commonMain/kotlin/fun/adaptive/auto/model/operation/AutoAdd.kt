package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.CollectionBackendBase
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoAdd(
    override val timestamp: LamportTimestamp,
    val itemId: ItemId,
    val wireFormatName: String?,
    val parentItemId: ItemId?,
    val payload: ByteArray
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean) {
        backend as CollectionBackendBase
        backend.add(this, commit)
    }

}