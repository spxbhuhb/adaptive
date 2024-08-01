package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.operation.AutoOperation
import hu.simplexion.adaptive.foundation.unsupported

abstract class AutoConnector {

    /**
     * Send the operation to the peer. The peer will call [AutoOperation.apply]
     * on its own backend to apply the operation.
     */
    open fun send(operation: AutoOperation, distribute: Boolean) {
        unsupported()
    }

}