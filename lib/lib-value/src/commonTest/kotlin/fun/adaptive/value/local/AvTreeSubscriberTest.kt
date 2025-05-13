package `fun`.adaptive.value.local

import `fun`.adaptive.backend.BackendAdapter
import `fun`.adaptive.utility.waitForReal
import `fun`.adaptive.value.*
import `fun`.adaptive.value.TestSupport.Companion.valueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class AvTreeSubscriberTest {

    val nodeMarker = "test-node"
    val childListMarker = "test-children"
    val parentRefLabel = "testParentRef"
    val childListRefLabel = "testChildrenRef"

    private lateinit var subscriber: TestTreeSubscriber

    // Test implementation of TreeSubscriber for testing purposes
    private inner class TestTreeSubscriber(
        backend: BackendAdapter,
        vararg condition: AvSubscribeCondition
    ) : AvTreeSubscriber<String, TestTreeItem>(
        backend,
        String::class,
        *condition
    ) {
        override val parentRefLabel = this@AvTreeSubscriberTest.parentRefLabel

        override fun newTreeItem(item: AvValue<String>, parentNode: Node<String, TestTreeItem>?): TestTreeItem {
            return TestTreeItem(item.spec)
        }

        override fun updateTreeItem(item: AvValue<String>, treeItem: TestTreeItem) {
            treeItem.value = item.spec
        }

        override fun updateChildren(treeItem: TestTreeItem, children: List<TestTreeItem>) {
            treeItem.children = children
        }
    }

    // Simple tree item class for testing
    private data class TestTreeItem(
        var value: String,
        var children: List<TestTreeItem> = emptyList()
    )

    @Test
    fun `should build tree structure from values`() = valueTest {
        // Create value IDs
        val rootId = AvValueId()
        val child1Id = AvValueId()
        val child2Id = AvValueId()
        val childListId = AvValueId()

        // Create subscriber
        subscriber = TestTreeSubscriber(clientBackend, avByMarker(nodeMarker), avByMarker(childListMarker))

        // Add listener to verify updates
        var receivedItems = emptyList<TestTreeItem>()
        subscriber.addListener { receivedItems = it }

        // Add root node
        waitForIdle {
            serverWorker.queueAdd(
                AvValue(
                    uuid = rootId,
                    markersOrNull = setOf(nodeMarker),
                    refsOrNull = mapOf(childListRefLabel to childListId),
                    spec = "Root"
                )
            )
        }

        // Add children
        waitForIdle {
            serverWorker.queueAddAll(
                AvValue(
                    uuid = child1Id,
                    markersOrNull = setOf(nodeMarker),
                    refsOrNull = mapOf(subscriber.parentRefLabel to rootId),
                    spec = "Child 1"
                ),
                AvValue(
                    uuid = child2Id,
                    markersOrNull = setOf(nodeMarker),
                    refsOrNull = mapOf(subscriber.parentRefLabel to rootId),
                    spec = "Child 2"
                ),
                // Add the child list
                AvValue(
                    uuid = childListId,
                    markersOrNull = setOf(childListMarker),
                    spec = AvRefListSpec(listOf(child1Id, child2Id)),
                    refsOrNull = mapOf(subscriber.parentRefLabel to rootId)
                )
            )
        }

        waitForReal(1.seconds) { receivedItems.isNotEmpty() }

        // Verify tree structure
        assertEquals(1, receivedItems.size) // Should have one root
        val root = receivedItems[0]
        assertEquals("Root", root.value)
        assertEquals(2, root.children.size)
        assertEquals("Child 1", root.children[0].value)
        assertEquals("Child 2", root.children[1].value)
    }

    @Test
    fun `should update existing tree items`() = valueTest {
        val rootId = AvValueId()

        subscriber = TestTreeSubscriber(clientBackend, avByMarker(nodeMarker), avByMarker(childListMarker))

        var receivedItems = emptyList<TestTreeItem>()
        subscriber.addListener { receivedItems = it }

        // Add initial root node
        waitForIdle {
            serverWorker.queueAdd(
                AvValue(
                    uuid = rootId,
                    markersOrNull = setOf(nodeMarker),
                    spec = "Initial Root"
                )
            )
        }

        waitForReal(1.seconds) { receivedItems.isNotEmpty() }
        assertEquals("Initial Root", receivedItems[0].value)

        // Update root node
        waitForIdle {
            serverWorker.queueUpdate(
                AvValue(
                    uuid = rootId,
                    markersOrNull = setOf(nodeMarker),
                    spec = "Updated Root"
                )
            )
        }

        waitForReal(1.seconds) { receivedItems[0].value == "Updated Root" }
        assertEquals("Updated Root", receivedItems[0].value)
    }

    @Test
    fun `should handle node removal`() = valueTest {
        val rootId = AvValueId()
        val child1Id = AvValueId()
        val childListId = AvValueId()

        subscriber = TestTreeSubscriber(clientBackend, avByMarker(nodeMarker), avByMarker(childListMarker))

        var receivedItems = emptyList<TestTreeItem>()
        subscriber.addListener { receivedItems = it }

        // Add root with one child
        waitForIdle {
            serverWorker.queueAddAll(
                AvValue(
                    uuid = rootId,
                    markersOrNull = setOf(nodeMarker),
                    refsOrNull = mapOf(childListRefLabel to childListId),
                    spec = "Root"
                ),
                AvValue(
                    uuid = child1Id,
                    markersOrNull = setOf(nodeMarker),
                    refsOrNull = mapOf(subscriber.parentRefLabel to rootId),
                    spec = "Child 1"
                ),
                AvValue(
                    uuid = childListId,
                    markersOrNull = setOf(childListMarker),
                    spec = AvRefListSpec(listOf(child1Id)),
                    refsOrNull = mapOf(subscriber.parentRefLabel to rootId)
                )
            )
        }

        waitForReal(1.seconds) { receivedItems.isNotEmpty() && receivedItems[0].children.isNotEmpty() }
        assertEquals(1, receivedItems[0].children.size)

        // Update child list to empty
        waitForIdle {
            serverWorker.queueUpdateAll(
                AvValue(
                    uuid = childListId,
                    spec = AvRefListSpec(emptyList()),
                    markersOrNull = setOf(childListMarker),
                    refsOrNull = mapOf(subscriber.parentRefLabel to rootId)
                ),
                AvValue(
                    uuid = child1Id,
                    markersOrNull = emptySet(),
                    refsOrNull = emptyMap(),
                    spec = "Child 1"
                )
            )
        }

        waitForReal(1.seconds) { receivedItems[0].children.isEmpty() }
        assertEquals(0, receivedItems[0].children.size)
    }
}