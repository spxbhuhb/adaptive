package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.BackendBase
import hu.simplexion.adaptive.auto.model.AutoPropertyValue

/**
 * @property  values  (property name, encoded property value)
 */
@Adat
class AutoModify(
    override val timestamp: LamportTimestamp,
    val itemId: ItemId,
    val values: List<AutoPropertyValue>
) : AutoOperation() {

    override fun apply(backend: BackendBase, commit: Boolean, distribute: Boolean) {
        backend.modify(this, commit, distribute)
    }

}