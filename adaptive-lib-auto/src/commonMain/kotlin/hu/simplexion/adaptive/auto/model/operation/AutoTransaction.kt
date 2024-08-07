package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.BackendBase

@Adat
class AutoTransaction(
    override val timestamp: LamportTimestamp,
    val additions: Set<ItemId>? = null,
    val removals: Set<ItemId>? = null,
    val modifications: List<AutoModify>? = null
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean, distribute: Boolean) {
        backend.transaction(this, commit, distribute)
    }

}