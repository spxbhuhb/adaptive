package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.model.AutoPropertyValue
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

/**
 * @property  values  (property name, encoded property value)
 */
@Adat
class AutoModify(
    override val timestamp: LamportTimestamp,
    val itemId: ItemId,
    val values: List<AutoPropertyValue>
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean) {
        backend.modify(this, commit)
    }

}