package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.auto.internal.backend.AutoCollectionBackend
import `fun`.adaptive.auto.model.LamportTimestamp

@Adat
class AutoEmpty(
    override val timestamp: LamportTimestamp
) : AutoOperation() {

    override fun apply(backend: AutoBackend, commit: Boolean) {
        backend as AutoCollectionBackend<*>
        backend.empty(this, commit)
    }

}