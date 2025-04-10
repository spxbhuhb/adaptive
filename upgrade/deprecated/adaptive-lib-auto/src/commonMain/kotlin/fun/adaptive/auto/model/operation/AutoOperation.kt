package `fun`.adaptive.auto.model.operation

import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.auto.internal.backend.AutoBackend

abstract class AutoOperation {

    abstract val timestamp: LamportTimestamp

    abstract fun apply(backend: AutoGeneric)

}