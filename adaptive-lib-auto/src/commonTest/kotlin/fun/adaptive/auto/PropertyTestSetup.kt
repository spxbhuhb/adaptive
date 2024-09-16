package `fun`.adaptive.auto

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.toArray
import `fun`.adaptive.auto.backend.TestData
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class PropertyTestSetup(
    initialData: Array<Any?>?
) {

    constructor(instance: AdatClass) : this(instance.toArray())

    val gid = UUID<BackendBase>()
    val itemId = LamportTimestamp(1, 1)
    val scope = CoroutineScope(Dispatchers.Default)
    val logger1 = getLogger("logger.1").enableFine()
    val logger2 = getLogger("logger.2").enableFine()

    val wireFormat = TestData.adatWireFormat

    val c1: BackendContext = BackendContext(AutoHandle(gid, 1, null), scope, logger1, Proto, wireFormat, LamportTimestamp(1, 1))
    val b1 = PropertyBackend(c1, itemId, null, initialData)

    val c2: BackendContext = BackendContext(AutoHandle(gid, 2, null), scope, logger2, Proto, wireFormat, LamportTimestamp(2, 0))
    val b2 = PropertyBackend(c2, itemId, null, null)

    fun connect() {
        b1.addPeer(DirectConnector(b2), c2.time)
        b2.addPeer(DirectConnector(b1), c1.time)
    }
}