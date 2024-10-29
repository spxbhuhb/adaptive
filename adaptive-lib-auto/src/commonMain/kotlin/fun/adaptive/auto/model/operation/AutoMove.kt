package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoMove(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val newParent: ItemId
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean) {
        //backend.move(timestamp, item, newParent)
    }

}