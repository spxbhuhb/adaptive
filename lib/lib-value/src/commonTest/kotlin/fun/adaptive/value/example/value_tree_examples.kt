package `fun`.adaptive.value.example

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.model.AvTreeSetup

//@example
suspend fun addRootNodeExample(valueWorker: AvValueWorker, treeSetup: AvTreeSetup) {
    valueWorker.execute {
        // Creates a value and adds it to the tree as a root node
        addTreeNode(
            parentId = null,  // root node
            treeSetup = treeSetup
        ) {
            AvValue(spec = "Root Node", markersOrNull = setOf(treeSetup.nodeMarker))
        }
    }
}

//@example
suspend fun addChildNodeExample(valueWorker: AvValueWorker, parentId: AvValueId, treeSetup: AvTreeSetup) {
    valueWorker.execute {

        // Creates an AvValue instance
        // Creating a value in itself does not add it to the value store nor to the tree.
        val child = AvValue(spec = "Child Node", markersOrNull = setOf(treeSetup.nodeMarker))

        // Adds the created value to the value store and also to the tree as a child of the parent node
        addTreeNode(
            parentId = parentId,
            child = child,
            treeSetup = treeSetup
        )
    }
}

//@example
suspend fun linkTreeNodeExample(valueWorker: AvValueWorker, parentId: AvValueId, treeSetup: AvTreeSetup) {
    valueWorker.execute {

        // Creates a value and adds it to the value store but does not add it to the tree.
        val child = addValue { AvValue(spec = "Child Node", markersOrNull = setOf(treeSetup.nodeMarker)) }

        // Links the created value to the tree as a child of the parent node.
        linkTreeNode(
            parentId = parentId,
            childId = child.uuid,
            treeSetup = treeSetup
        )
    }
}

//@example
suspend fun removeTreeNodeExample(valueWorker: AvValueWorker, parentId: AvValueId, childId: AvValueId, treeSetup: AvTreeSetup) {
    valueWorker.execute {
        // Removes a child node from its parent in the tree
        removeTreeNode(
            parentId = parentId,
            childId = childId,
            treeSetup = treeSetup
        )
    }
}

//@example
suspend fun moveTreeNodeUpExample(valueWorker: AvValueWorker, childId: AvValueId, treeSetup: AvTreeSetup) {
    valueWorker.execute {
        // Moves a node up in the ordering of its siblings in the tree
        moveTreeNodeUp(
            childId = childId,
            treeSetup = treeSetup
        )
    }
}

//@example
suspend fun moveTreeNodeDownExample(valueWorker: AvValueWorker, childId: AvValueId, treeSetup: AvTreeSetup) {
    valueWorker.execute {
        // Moves a node down in the ordering of its siblings in the tree
        moveTreeNodeDown(
            childId = childId,
            treeSetup = treeSetup
        )
    }
}

//@example
suspend fun getTreeChildIdsExample(valueWorker: AvValueWorker, parentId: AvValueId, treeSetup: AvTreeSetup) {
    valueWorker.execute {
        // Retrieves a list of IDs for all direct children of a parent node
        val children: List<AvValueId> = getTreeChildIds(parentId, treeSetup)
        println("Children: $children")
    }
}

//@example
suspend fun getTreeSiblingIdsExample(valueWorker: AvValueWorker, childId: AvValueId, treeSetup: AvTreeSetup) {
    valueWorker.execute {
        // Retrieves a list of IDs for all siblings of a node (nodes sharing the same parent)
        val siblings: List<AvValueId> = getTreeSiblingIds(childId, treeSetup)
        println("Siblings: $siblings")
    }
}



