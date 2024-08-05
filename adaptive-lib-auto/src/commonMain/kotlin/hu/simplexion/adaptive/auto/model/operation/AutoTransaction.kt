package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend

@Adat
class AutoTransaction(
    override val timestamp: LamportTimestamp,
    val modify: List<AutoModify>
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean, distribute: Boolean) {
        backend.transaction(this, commit, distribute)
    }

}