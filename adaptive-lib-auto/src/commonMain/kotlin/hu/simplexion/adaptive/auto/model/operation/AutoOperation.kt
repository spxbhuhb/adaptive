package hu.simplexion.adaptive.auto.model.operation

import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AbstractBackend

abstract class AutoOperation {

    abstract val timestamp: LamportTimestamp

    abstract fun apply(backend: AbstractBackend, commit: Boolean, distribute: Boolean)

}