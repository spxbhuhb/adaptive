package `fun`.adaptive.value.store

import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvRefListSpec
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.model.AvTreeDef
import `fun`.adaptive.value.standaloneTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TreeTest {

    @Test
    fun testAddRootNode() = standaloneTest { worker ->

        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")
        val rootNode = AvValue(spec = "single top")

        worker.execute {
            addValue(rootNode)
            linkTreeNode(treeDef, null, rootNode.uuid)
        }

        val readback = worker.get<String>(rootNode.uuid)
        assertEquals(rootNode.uuid, readback.uuid)
        assertEquals(rootNode.spec, readback.spec)

        val rootList = worker.queryByMarker("rootList")
        assertEquals(1, rootList.size)
        assertEquals(rootNode.uuid, rootList.first().checkSpec<AvRefListSpec>().spec.refs.first())
    }

    @Test
    fun testAddTreeNode() = standaloneTest { worker ->
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create the child node
        val childNode = AvValue(spec = "child")

        worker.execute {
            // Add both nodes to the store
            addValue(parentNode)
            addValue(childNode)

            // Add parent as root node
            linkTreeNode(treeDef, null, parentNode.uuid)

            // Add child to parent
            linkTreeNode(treeDef, parentNode.uuid, childNode.uuid)
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
        val childListRef = parentReadback.refIdOrNull(treeDef.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify child list contains child
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(1, childList.spec.refs.size)
        assertEquals(childNode.uuid, childList.spec.refs.first())
    }

    @Test
    fun testRemoveTreeNode() = standaloneTest { worker ->
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

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
            linkTreeNode(treeDef, null, parentNode.uuid)

            // Add children to parent
            linkTreeNode(treeDef, parentNode.uuid, childNode1.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode2.uuid)

            // Remove first child
            removeTreeNode(treeDef, parentNode.uuid, childNode1.uuid)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(treeDef.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify child list contains only the second child
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(1, childList.spec.refs.size)
        assertEquals(childNode2.uuid, childList.spec.refs.first())
    }

    @Test
    fun testRemoveRootTreeNode() = standaloneTest { worker ->
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

        // Create root nodes
        val rootNode1 = AvValue(spec = "root1")
        val rootNode2 = AvValue(spec = "root2")

        worker.execute {
            // Add nodes to the store
            addValue(rootNode1)
            addValue(rootNode2)

            // Add both as root nodes
            linkTreeNode(treeDef, null, rootNode1.uuid)
            linkTreeNode(treeDef, null, rootNode2.uuid)

            // Remove first root node
            removeTreeNode(treeDef, null, rootNode1.uuid)
        }

        // Verify root list contains only the second root
        val rootList = worker.queryByMarker("rootList")
        assertEquals(1, rootList.size)
        assertEquals(1, rootList.first().checkSpec<AvRefListSpec>().spec.refs.size)
        assertEquals(rootNode2.uuid, rootList.first().checkSpec<AvRefListSpec>().spec.refs.first())
    }

    @Test
    fun testMoveTreeNodeUp() = standaloneTest { worker ->
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

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
            linkTreeNode(treeDef, null, parentNode.uuid)

            // Add children to parent in order
            linkTreeNode(treeDef, parentNode.uuid, childNode1.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode2.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode3.uuid)

            // Move the second child up
            moveTreeNodeUp(treeDef, childNode2.uuid)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(treeDef.childListRefLabel)
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
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

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
            linkTreeNode(treeDef, null, parentNode.uuid)

            // Add children to parent in order
            linkTreeNode(treeDef, parentNode.uuid, childNode1.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode2.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode3.uuid)

            // Move the second child down
            moveTreeNodeDown(treeDef, childNode2.uuid)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(treeDef.childListRefLabel)
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
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

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
            linkTreeNode(treeDef, null, parentNode.uuid)

            // Add children to parent in order
            linkTreeNode(treeDef, parentNode.uuid, childNode1.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode2.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode3.uuid)
        }

        // Get child list reference
        val childListRef = worker.get<String>(parentNode.uuid).refIdOrNull(treeDef.childListRefLabel)
        assertTrue(childListRef != null)

        // Verify child list contains all three children
        val childList = worker.get<AvRefListSpec>(childListRef)
        assertEquals(3, childList.spec.refs.size)
        assertEquals(childNode1.uuid, childList.spec.refs[0])
        assertEquals(childNode2.uuid, childList.spec.refs[1])
        assertEquals(childNode3.uuid, childList.spec.refs[2])

        // Get siblings of the second child
        val siblings = worker.execute { getTreeSiblingIds(treeDef, childNode2.uuid) }

        // Verify siblings list contains all three children
        assertEquals(3, siblings.size)
        assertEquals(childNode1.uuid, siblings[0])
        assertEquals(childNode2.uuid, siblings[1])
        assertEquals(childNode3.uuid, siblings[2])
    }

    @Test
    fun testGetTreeChildIds() = standaloneTest { worker ->
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

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
            linkTreeNode(treeDef, null, parentNode.uuid)

            // Add children to parent
            linkTreeNode(treeDef, parentNode.uuid, childNode1.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode2.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode3.uuid)
        }

        // Test getting children of parent node
        val children = worker.execute { getTreeChildIds(treeDef, parentNode.uuid) }

        // Verify children list contains all three children in the correct order
        assertEquals(3, children.size)
        assertEquals(childNode1.uuid, children[0])
        assertEquals(childNode2.uuid, children[1])
        assertEquals(childNode3.uuid, children[2])

        // Test getting children of a node without children
        val emptyChildren = worker.execute { getTreeChildIds(treeDef, childNode1.uuid) }
        assertTrue(emptyChildren.isEmpty())

        // Test getting children of non-existent node
        val nonExistentChildren = worker.execute { getTreeChildIds(treeDef, uuid4()) }
        assertTrue(nonExistentChildren.isEmpty())
    }
    
    @Test
    fun testMoveTreeNodeAfter() = standaloneTest { worker ->
        val treeDef = AvTreeDef("node", "childList", "parentRef", "childListRef", "rootList")

        // Create parent node
        val parentNode = AvValue(spec = "parent")

        // Create child nodes
        val childNode1 = AvValue(spec = "child1")
        val childNode2 = AvValue(spec = "child2")
        val childNode3 = AvValue(spec = "child3")
        val childNode4 = AvValue(spec = "child4")

        worker.execute {
            // Add nodes to the store
            addValue(parentNode)
            addValue(childNode1)
            addValue(childNode2)
            addValue(childNode3)
            addValue(childNode4)

            // Add parent as root node
            linkTreeNode(treeDef, null, parentNode.uuid)

            // Add children to parent in order
            linkTreeNode(treeDef, parentNode.uuid, childNode1.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode2.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode3.uuid)
            linkTreeNode(treeDef, parentNode.uuid, childNode4.uuid)
        }

        // Test case 1: Move to top (afterId = null)
        worker.execute {
            moveTreeNodeAfter(treeDef, childNode3.uuid, null)
        }

        // Verify the order after moving to top
        var childList = worker.execute { 
            val childListRef = ref<AvRefListSpec>(parentNode.uuid, treeDef.childListRefLabel)
            childListRef.spec.refs
        }
        
        assertEquals(4, childList.size)
        assertEquals(childNode3.uuid, childList[0])
        assertEquals(childNode1.uuid, childList[1])
        assertEquals(childNode2.uuid, childList[2])
        assertEquals(childNode4.uuid, childList[3])

        // Test case 2: Move after a specific node
        worker.execute {
            moveTreeNodeAfter(treeDef, childNode4.uuid, childNode1.uuid)
        }

        // Verify the order after moving after childNode1
        childList = worker.execute { 
            val childListRef = ref<AvRefListSpec>(parentNode.uuid, treeDef.childListRefLabel)
            childListRef.spec.refs
        }
        
        assertEquals(4, childList.size)
        assertEquals(childNode3.uuid, childList[0])
        assertEquals(childNode1.uuid, childList[1])
        assertEquals(childNode4.uuid, childList[2])
        assertEquals(childNode2.uuid, childList[3])

        // Test case 3: No-op when already in correct position
        worker.execute {
            moveTreeNodeAfter(treeDef, childNode4.uuid, childNode1.uuid)
        }

        // Verify the order remains the same
        childList = worker.execute { 
            val childListRef = ref<AvRefListSpec>(parentNode.uuid, treeDef.childListRefLabel)
            childListRef.spec.refs
        }
        
        assertEquals(4, childList.size)
        assertEquals(childNode3.uuid, childList[0])
        assertEquals(childNode1.uuid, childList[1])
        assertEquals(childNode4.uuid, childList[2])
        assertEquals(childNode2.uuid, childList[3])

        // Test case 4: Move when source index is before target index
        worker.execute {
            moveTreeNodeAfter(treeDef, childNode3.uuid, childNode4.uuid)
        }

        // Verify the order after moving childNode3 after childNode4
        childList = worker.execute { 
            val childListRef = ref<AvRefListSpec>(parentNode.uuid, treeDef.childListRefLabel)
            childListRef.spec.refs
        }
        
        assertEquals(4, childList.size)
        assertEquals(childNode1.uuid, childList[0])
        assertEquals(childNode4.uuid, childList[1])
        assertEquals(childNode3.uuid, childList[2])
        assertEquals(childNode2.uuid, childList[3])
    }
}
