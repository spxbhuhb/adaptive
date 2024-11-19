package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

/**
 * @property  [syncTime]  When true, the receiving backend have to update its timestamp
 *                        to [syncTime], when false, it should not. Differentiates
 *                        between removal during sync and removals after synced.
 *                        During sync the time should not be changed to let other
 *                        operations execute.
 */
@Adat
class AutoRemove(
    override val timestamp: LamportTimestamp,
    val syncTime : Boolean,
    val itemIds: Set<ItemId>
) : AutoOperation() {

    override fun apply(backend: AutoGeneric) {
        backend.remove(this)
    }

}