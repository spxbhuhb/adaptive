package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend

@Adat
class AutoAdd(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val parent: ItemId
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean, distribute: Boolean) {
        backend.add(timestamp, item, parent)
    }

}