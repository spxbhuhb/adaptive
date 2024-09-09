package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.internal.backend.BackendBase

abstract class AutoOperation {

    abstract val timestamp: LamportTimestamp

    abstract fun apply(backend: BackendBase, commit: Boolean)

}