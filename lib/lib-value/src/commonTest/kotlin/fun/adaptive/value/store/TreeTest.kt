package `fun`.adaptive.value.store

import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvRefListSpec
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.withSpec
import `fun`.adaptive.value.ValueTestSupport.Companion.standaloneTest
import `fun`.adaptive.value.model.AvTreeSetup
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TreeTest {

    @Test
    fun testAddRootNode() = standaloneTest { worker ->

        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")
        val rootNode = AvValue(spec = "single top")

        worker.execute {
            addValue(rootNode)
            addTreeNode(null, rootNode.uuid, setup)
        }

        val readback = worker.get<String>(rootNode.uuid)
        assertEquals(rootNode.uuid, readback.uuid)
        assertEquals(rootNode.spec, readback.spec)

        val rootList = worker.queryByMarker("rootList")
        assertEquals(1, rootList.size)
        assertEquals(rootNode.uuid, rootList.first().withSpec<AvRefListSpec>().spec.refs.first())
    }

    @Test
    fun testAddTreeNode() = standaloneTest { worker ->
        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create the child node
        val childNode = AvValue(spec = "child")

        worker.execute {
            // Add both nodes to the store
            addValue(parentNode)
            addValue(childNode)

            // Add parent as root node
            addTreeNode(null, parentNode.uuid, setup)

            // Add child to parent
            addTreeNode(parentNode.uuid, childNode.uuid, setup)
        }

        // Verify parent node
        val parentReadback = worker.get<String>(parentNode.uuid)
        assertEquals(parentNode.uuid, parentReadback.uuid)
        assertEquals(parentNode.spec, parentReadback.spec)

        // Verify child node
        val childReadback = worker.get<String>(childNode.uuid)
        assertEquals(childNode.uuid, childReadback.uuid)
        assertEquals(childNode.spec, childReadback.spec)

        // Verify parent has child list reference
        val childListRef = parentReadback.refIdOrNull(setup.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify child list contains child
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(1, childList.spec.refs.size)
        assertEquals(childNode.uuid, childList.spec.refs.first())
    }

    @Test
    fun testRemoveTreeNode() = standaloneTest { worker ->
        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create child nodes
        val childNode1 = AvValue(spec = "child1")
        val childNode2 = AvValue(spec = "child2")

        worker.execute {
            // Add nodes to the store
            addValue(parentNode)
            addValue(childNode1)
            addValue(childNode2)

            // Add parent as root node
            addTreeNode(null, parentNode.uuid, setup)

            // Add children to parent
            addTreeNode(parentNode.uuid, childNode1.uuid, setup)
            addTreeNode(parentNode.uuid, childNode2.uuid, setup)

            // Remove first child
            removeTreeNode(parentNode.uuid, childNode1.uuid, setup)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(setup.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify child list contains only the second child
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(1, childList.spec.refs.size)
        assertEquals(childNode2.uuid, childList.spec.refs.first())
    }

    @Test
    fun testRemoveRootTreeNode() = standaloneTest { worker ->
        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

        // Create root nodes
        val rootNode1 = AvValue(spec = "root1")
        val rootNode2 = AvValue(spec = "root2")

        worker.execute {
            // Add nodes to the store
            addValue(rootNode1)
            addValue(rootNode2)

            // Add both as root nodes
            addTreeNode(null, rootNode1.uuid, setup)
            addTreeNode(null, rootNode2.uuid, setup)

            // Remove first root node
            removeTreeNode(null, rootNode1.uuid, setup)
        }

        // Verify root list contains only the second root
        val rootList = worker.queryByMarker("rootList")
        assertEquals(1, rootList.size)
        assertEquals(1, rootList.first().withSpec<AvRefListSpec>().spec.refs.size)
        assertEquals(rootNode2.uuid, rootList.first().withSpec<AvRefListSpec>().spec.refs.first())
    }

    @Test
    fun testMoveTreeNodeUp() = standaloneTest { worker ->
        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create child nodes
        val childNode1 = AvValue(spec = "child1")
        val childNode2 = AvValue(spec = "child2")
        val childNode3 = AvValue(spec = "child3")

        worker.execute {
            // Add nodes to the store
            addValue(parentNode)
            addValue(childNode1)
            addValue(childNode2)
            addValue(childNode3)

            // Add parent as root node
            addTreeNode(null, parentNode.uuid, setup)

            // Add children to parent in order
            addTreeNode(parentNode.uuid, childNode1.uuid, setup)
            addTreeNode(parentNode.uuid, childNode2.uuid, setup)
            addTreeNode(parentNode.uuid, childNode3.uuid, setup)

            // Move the second child up
            moveTreeNodeUp(childNode2.uuid, setup)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(setup.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify that the child list has the correct order after moving up
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(3, childList.spec.refs.size)
        assertEquals(childNode2.uuid, childList.spec.refs[0])
        assertEquals(childNode1.uuid, childList.spec.refs[1])
        assertEquals(childNode3.uuid, childList.spec.refs[2])
    }

    @Test
    fun testMoveTreeNodeDown() = standaloneTest { worker ->
        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create child nodes
        val childNode1 = AvValue(spec = "child1")
        val childNode2 = AvValue(spec = "child2")
        val childNode3 = AvValue(spec = "child3")

        worker.execute {
            // Add nodes to the store
            addValue(parentNode)
            addValue(childNode1)
            addValue(childNode2)
            addValue(childNode3)

            // Add parent as root node
            addTreeNode(null, parentNode.uuid, setup)

            // Add children to parent in order
            addTreeNode(parentNode.uuid, childNode1.uuid, setup)
            addTreeNode(parentNode.uuid, childNode2.uuid, setup)
            addTreeNode(parentNode.uuid, childNode3.uuid, setup)

            // Move the second child down
            moveTreeNodeDown(childNode2.uuid, setup)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(setup.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify that the child list has the correct order after moving down
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(3, childList.spec.refs.size)
        assertEquals(childNode1.uuid, childList.spec.refs[0])
        assertEquals(childNode3.uuid, childList.spec.refs[1])
        assertEquals(childNode2.uuid, childList.spec.refs[2])
    }

    @Test
    fun testGetTreeSiblingIds() = standaloneTest { worker ->
        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create child nodes
        val childNode1 = AvValue(spec = "child1")
        val childNode2 = AvValue(spec = "child2")
        val childNode3 = AvValue(spec = "child3")

        worker.execute {
            // Add nodes to the store
            addValue(parentNode)
            addValue(childNode1)
            addValue(childNode2)
            addValue(childNode3)

            // Add parent as root node
            addTreeNode(null, parentNode.uuid, setup)

            // Add children to parent in order
            addTreeNode(parentNode.uuid, childNode1.uuid, setup)
            addTreeNode(parentNode.uuid, childNode2.uuid, setup)
            addTreeNode(parentNode.uuid, childNode3.uuid, setup)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(setup.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify child list contains all three children
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(3, childList.spec.refs.size)
        assertEquals(childNode1.uuid, childList.spec.refs[0])
        assertEquals(childNode2.uuid, childList.spec.refs[1])
        assertEquals(childNode3.uuid, childList.spec.refs[2])

        // Get siblings of the second child
        val siblings = worker.execute { getTreeSiblingIds(childNode2.uuid, setup) }

        // Verify siblings list contains all three children
        assertEquals(3, siblings.size)
        assertEquals(childNode1.uuid, siblings[0])
        assertEquals(childNode2.uuid, siblings[1])
        assertEquals(childNode3.uuid, siblings[2])
    }

    @Test
    fun testGetTreeChildIds() = standaloneTest { worker ->
        val setup = AvTreeSetup("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create child nodes
        val childNode1 = AvValue(spec = "child1")
        val childNode2 = AvValue(spec = "child2")
        val childNode3 = AvValue(spec = "child3")

        worker.execute {
            // Add nodes to the store
            addValue(parentNode)
            addValue(childNode1)
            addValue(childNode2)
            addValue(childNode3)

            // Add parent as root node
            addTreeNode(null, parentNode.uuid, setup)

            // Add children to parent
            addTreeNode(parentNode.uuid, childNode1.uuid, setup)
            addTreeNode(parentNode.uuid, childNode2.uuid, setup)
            addTreeNode(parentNode.uuid, childNode3.uuid, setup)
        }

        // Test getting children of parent node
        val children = worker.execute { getTreeChildIds(parentNode.uuid, setup) }

        // Verify children list contains all three children in the correct order
        assertEquals(3, children.size)
        assertEquals(childNode1.uuid, children[0])
        assertEquals(childNode2.uuid, children[1])
        assertEquals(childNode3.uuid, children[2])

        // Test getting children of a node without children
        val emptyChildren = worker.execute { getTreeChildIds(childNode1.uuid, setup) }
        assertTrue(emptyChildren.isEmpty())

        // Test getting children of non-existent node
        val nonExistentChildren = worker.execute { getTreeChildIds(uuid4(), setup) }
        assertTrue(nonExistentChildren.isEmpty())
    }
}
