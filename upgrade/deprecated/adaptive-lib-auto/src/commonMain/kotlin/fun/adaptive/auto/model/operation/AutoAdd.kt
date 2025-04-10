package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoAdd(
    override val timestamp: LamportTimestamp,
    val itemId: ItemId,
    val wireFormatName: String?,
    val parentItemId: ItemId?,
    val payload: ByteArray,
    val propertyTimes : List<LamportTimestamp>
) : AutoOperation() {

    override fun apply(instance: AutoGeneric) {
        instance.remoteAdd(this)
    }

}