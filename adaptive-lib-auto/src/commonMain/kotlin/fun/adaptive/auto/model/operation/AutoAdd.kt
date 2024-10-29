package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
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

    override fun apply(backend: AutoBackend, commit: Boolean) {
        backend as AutoCollectionBackend<*>
        backend.add(this, commit)
    }

}