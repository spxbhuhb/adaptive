package hu.simplexion.adaptive.auto.connector

import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend

class DirectConnector(
    val peer: AutoBackend
) : AutoConnector() {

    override fun modify(timestamp: LamportTimestamp, item: ItemId, propertyName: String, propertyValue: Any?) {
        peer.modify(timestamp, item, propertyName, propertyValue)
    }

    override fun insert(timestamp: LamportTimestamp, item: ItemId, origin: ItemId, left: ItemId, right: ItemId) {
        peer.insert(timestamp, item, origin, left, right)
    }

    override fun add(timestamp: LamportTimestamp, item: ItemId, parent: ItemId) {
        peer.add(timestamp, item, parent)
    }

    override fun move(timestamp: LamportTimestamp, item: ItemId, newParent: ItemId) {
        peer.move(timestamp, item, newParent)
    }

    override fun remove(timestamp: LamportTimestamp, item: ItemId) {
        peer.remove(timestamp, item)
    }

}