package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.ItemId
import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.backend.BackendBase
import `fun`.adaptive.auto.model.AutoPropertyValue

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