package `fun`.adaptive.auto.test.collection.node

import `fun`.adaptive.auto.api.autoCollectionNode
import `fun`.adaptive.auto.api.autoCollectionOrigin
import `fun`.adaptive.auto.test.support.AutoTest.Companion.autoTest
import `fun`.adaptive.auto.test.support.CollectionTestSetupService
import `fun`.adaptive.auto.test.support.content_12
import `fun`.adaptive.utility.clearedTestPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds

class RestartTest {

    @Test
    fun `restart, direct origin stopped`() = runTest {
        withContext(Dispatchers.Default) {
            val origin = autoCollectionOrigin(content_12)

            val node = autoCollectionNode(origin).ensureValue()
            assertEquals(content_12, node.value)

            origin.stop()
            node.stop()

            // FIXME this test should fail think
            val node2 = autoCollectionNode(origin).ensureValue()
            assertEquals(content_12, node2.value)
        }
    }

    @Test
    fun `restart, service origin stopped, non-persistent`() = autoTest {
        val origin = autoCollectionOrigin(content_12, serverWorker)

        val node = autoCollectionNode(clientWorker) { origin.connectInfo() }.ensureValue()
        assertEquals(content_12, node.value)

        origin.stop()
        node.stop()

        assertFailsWith<TimeoutCancellationException> {
            autoCollectionNode(clientWorker) { origin.connectInfo() }.ensureValue(500.milliseconds)
        }
    }

    @Test
    fun `restart, service origin stopped, persistent`() = autoTest {
        val testPath = clearedTestPath()

        with(CollectionTestSetupService(this, testPath, content_12)) {
            node.ensureValue()

            origin.stop()
            node.stop()

            val node2 = autoCollectionNode(clientWorker, persistence = nodePersistence) { origin.connectInfo() }

            println(node2.valueOrNull)
            node2.ensureValue()

            assertEquals(content_12, node2.value)
        }
    }

}