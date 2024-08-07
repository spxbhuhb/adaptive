package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.BackendBase
import hu.simplexion.adaptive.auto.backend.CollectionBackendBase

@Adat
class AutoAdd(
    override val timestamp: LamportTimestamp,
    val itemId: ItemId,
    val parentItemId: ItemId?
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean, distribute: Boolean) {
        backend as CollectionBackendBase
        backend.add(itemId, parentItemId, commit, distribute)
    }

}