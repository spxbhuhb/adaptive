package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoSyncEnd(
    override val timestamp: LamportTimestamp
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean) {
        backend.syncEnd(this, commit)
    }

}