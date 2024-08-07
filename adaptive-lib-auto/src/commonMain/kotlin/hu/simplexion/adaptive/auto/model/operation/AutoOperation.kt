package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.BackendBase

abstract class AutoOperation {

    abstract val timestamp: LamportTimestamp

    abstract fun apply(backend: BackendBase, commit: Boolean, distribute: Boolean)

}