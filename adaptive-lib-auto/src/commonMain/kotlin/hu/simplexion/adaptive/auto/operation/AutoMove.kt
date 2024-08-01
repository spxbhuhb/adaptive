package hu.simplexion.adaptive.auto.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend

@Adat
class AutoMove(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val newParent: ItemId
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean, distribute: Boolean) {
        backend.move(timestamp, item, newParent)
    }

}