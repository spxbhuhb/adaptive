package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.foundation.unsupported

abstract class AutoConnector {

    // --------------------------------------------------------------------------------
    // Property change (common for all)
    // --------------------------------------------------------------------------------

    abstract fun modify(timestamp: LamportTimestamp, item: ItemId, propertyName: String, propertyValue: Any?)

    // --------------------------------------------------------------------------------
    // List specific
    // --------------------------------------------------------------------------------

    open fun insert(timestamp: LamportTimestamp, item: ItemId, origin: ItemId, left: ItemId, right: ItemId) {
        unsupported()
    }

    // --------------------------------------------------------------------------------
    // Tree specific
    // --------------------------------------------------------------------------------

    open fun add(timestamp: LamportTimestamp, item: ItemId, parent: ItemId) {
        unsupported()
    }

    open fun move(timestamp: LamportTimestamp, item: ItemId, newParent: ItemId) {
        unsupported()
    }

    // --------------------------------------------------------------------------------
    // Common for list and tree
    // --------------------------------------------------------------------------------

    open fun remove(timestamp: LamportTimestamp, item: ItemId) {
        unsupported()
    }

}