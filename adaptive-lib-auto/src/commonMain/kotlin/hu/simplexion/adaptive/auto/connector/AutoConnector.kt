package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.model.operation.AutoOperation

abstract class AutoConnector {

    abstract fun send(operation: AutoOperation)

}