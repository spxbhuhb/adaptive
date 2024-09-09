package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoMove(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val newParent: ItemId
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean) {
        //backend.move(timestamp, item, newParent)
    }

}