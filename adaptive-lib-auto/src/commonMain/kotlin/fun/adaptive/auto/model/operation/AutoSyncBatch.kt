package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoSyncBatch(
    override val timestamp: LamportTimestamp,
    val additions: List<AutoAdd>,
    val updates: List<AutoUpdate>
) : AutoOperation() {

    override fun apply(instance: AutoGeneric) {
        instance.syncBatch(this)
    }

}