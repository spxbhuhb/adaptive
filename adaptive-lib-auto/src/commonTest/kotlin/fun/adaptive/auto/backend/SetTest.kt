package `fun`.adaptive.auto.backend

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.auto.internal.backend.BackendBase
import `fun`.adaptive.auto.internal.backend.BackendContext
import `fun`.adaptive.auto.internal.backend.PropertyBackend
import `fun`.adaptive.auto.internal.backend.SetBackend
import `fun`.adaptive.auto.internal.connector.DirectConnector
import `fun`.adaptive.auto.internal.frontend.AdatClassListFrontend
import `fun`.adaptive.auto.model.AutoHandle
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.auto.model.LamportTimestamp
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.wireformat.api.Proto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class SetTest {

    @Test
    fun basic() {
        val gid = UUID<BackendBase>()
        val testData = TestData(12, "ab")

        runTest {
            val scope = CoroutineScope(Dispatchers.Default)
            val logger1 = getLogger("logger.1")
            val logger2 = getLogger("logger.2")

            val c1 = BackendContext<TestData>(AutoHandle(gid, 1, null), scope, logger1, Proto, TestData.adatWireFormat, LamportTimestamp(1, 0))
            val b1 = SetBackend(c1)
            val f1 = AdatClassListFrontend<TestData>(b1)
            b1.frontend = f1

            val c2 = BackendContext<TestData>(AutoHandle(gid, 2, null), scope, logger2, Proto, TestData.adatWireFormat, LamportTimestamp(2, 0))
            val b2 = SetBackend(c2)
            val f2 = AdatClassListFrontend<TestData>(b2)
            b2.frontend = f2

            b1.addPeer(DirectConnector(b2), c2.time)
            b2.addPeer(DirectConnector(b1), c1.time)

            waitForSync(b1, b2)

            f1.add(testData)
            f2.assertEquals(f1)

            val itemId1 = f1.values.first().adatContext?.id as ItemId
            f1.modify(itemId1, "i", 23)
            f2.assertEquals(f1)

            f2.modify(itemId1, "i", 34)
            f1.assertEquals(f2)

            val t2 = TestData(54, "ef")
            f1.add(t2)
            f2.assertEquals(f1)

            val t3 = TestData(67, "gh")
            f2.add(t3)
            f1.assertEquals(f2)

            f1.remove(itemId1)
            f2.assertEquals(f1)
        }
    }

    @Test
    fun preloaded() {
        val gid = UUID<BackendBase>()
        val testData = TestData(12, "ab")

        runTest {
            val scope = CoroutineScope(Dispatchers.Default)
            val logger1 = getLogger("logger.1")
            val logger2 = getLogger("logger.2")

            val c1 = BackendContext<TestData>(AutoHandle(gid, 1, null), scope, logger1, Proto, TestData.adatWireFormat, LamportTimestamp(1, 0))
            val b1 = SetBackend(c1)
            val f1 = AdatClassListFrontend<TestData>(b1)
            b1.frontend = f1

            val c2 = BackendContext<TestData>(AutoHandle(gid, 2, null), scope, logger2, Proto, TestData.adatWireFormat, LamportTimestamp(2, 0))
            val b2 = SetBackend(c2)
            val f2 = AdatClassListFrontend<TestData>(b2)
            b2.frontend = f2

            f1.add(testData)

            b1.addPeer(DirectConnector(b2), c2.time)
            b2.addPeer(DirectConnector(b1), c1.time)

            f2.assertEquals(f1) { f2.values.isNotEmpty() }
        }
    }


    suspend fun waitForSync(b1: BackendBase, b2: BackendBase, condition: (() -> Boolean)? = null) {
        withContext(Dispatchers.Default) {
            withTimeout(1000) {
                if (condition != null) {
                    while (! condition()) {
                        delay(10)
                    }
                } else {
                    while (b1.context.time.timestamp != b2.context.time.timestamp) {
                        delay(10)
                    }
                }
            }
        }
    }

    suspend fun <A : AdatClass> AdatClassListFrontend<A>.assertEquals(expected: AdatClassListFrontend<A>, condition: (() -> Boolean)? = null) {
        waitForSync(this.backend, expected.backend, condition)
        this.backend.assertEquals(expected.backend)
        assertEquals(expected.values, this.values)
    }

    suspend fun SetBackend<*>.assertEquals(expected: SetBackend<*>) {
        waitForSync(this, expected)

        assertEquals(expected.additions, this.additions)
        assertEquals(expected.removals, this.removals)
        assertEquals(expected.items.size, this.items.size)
        for ((key, value) in this.items) {
            value.assertEquals(expected.items[key] !!)
        }
    }

    suspend fun PropertyBackend<*>.assertEquals(expected: PropertyBackend<*>) {
        waitForSync(this, expected)

        assertEquals(expected.itemId, this.itemId)
        assertContentEquals(expected.values, this.values)
    }
}