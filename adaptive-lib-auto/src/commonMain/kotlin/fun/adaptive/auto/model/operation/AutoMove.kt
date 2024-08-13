package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.ItemId
import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.backend.BackendBase

@Adat
class AutoMove(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val newParent: ItemId
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean, distribute: Boolean) {
        //backend.move(timestamp, item, newParent)
    }

}