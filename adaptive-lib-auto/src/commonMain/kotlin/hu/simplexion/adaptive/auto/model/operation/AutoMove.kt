package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AbstractBackend

@Adat
class AutoMove(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val newParent: ItemId
) : AutoOperation() {

    override fun apply(backend: AbstractBackend, commit: Boolean, distribute: Boolean) {
        backend.move(timestamp, item, newParent)
    }

}