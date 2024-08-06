package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend

@Adat
class AutoSyncEnd(
    override val timestamp: LamportTimestamp
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean, distribute: Boolean) {
        backend.syncEnd(timestamp)
    }

}