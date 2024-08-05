package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend

@Adat
class AutoInsert(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val origin: ItemId,
    val left: ItemId,
    val right: ItemId
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean, distribute: Boolean) {
        backend.insert(timestamp, item, origin, left, right)
    }

}