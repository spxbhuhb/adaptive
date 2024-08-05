package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend

@Adat
class AutoModify(
    override val timestamp: LamportTimestamp,
    val item: ItemId,
    val propertyName: String,
    val propertyValue: ByteArray
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean, distribute: Boolean) {
        backend.modify(this, commit, distribute)
    }

}