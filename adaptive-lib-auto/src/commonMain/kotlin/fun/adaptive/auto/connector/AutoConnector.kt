package `fun`.adaptive.auto.connector

import `fun`.adaptive.auto.model.operation.AutoOperation

abstract class AutoConnector {

    abstract fun send(operation: AutoOperation)

}