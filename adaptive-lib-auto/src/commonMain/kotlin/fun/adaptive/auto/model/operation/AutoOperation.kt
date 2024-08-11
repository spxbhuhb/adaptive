package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.auto.LamportTimestamp
import `fun`.adaptive.auto.backend.BackendBase

abstract class AutoOperation {

    abstract val timestamp: LamportTimestamp

    abstract fun apply(backend: BackendBase, commit: Boolean, distribute: Boolean)

}