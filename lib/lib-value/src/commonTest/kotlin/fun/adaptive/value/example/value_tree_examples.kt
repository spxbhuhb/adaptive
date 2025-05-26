package `fun`.adaptive.value.example

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId
import `fun`.adaptive.value.AvValueWorker
import `fun`.adaptive.value.model.AvTreeDef

//@example
suspend fun addRootNodeExample(valueWorker: AvValueWorker, treeDef: AvTreeDef) {
    valueWorker.execute {
        // Creates a value and adds it to the tree as a root node
        addTreeNode(
            treeDef = treeDef,
            parentId = null   // root node
        ) {
            AvValue(spec = "Root Node", markersOrNull = setOf(treeDef.nodeMarker))
        }
    }
}

//@example
suspend fun addChildNodeExample(valueWorker: AvValueWorker, parentId: AvValueId, treeDef: AvTreeDef) {
    valueWorker.execute {

        // Creates an AvValue instance
        // Creating a value in itself does not add it to the value store nor to the tree.
        val child = AvValue(spec = "Child Node", markersOrNull = setOf(treeDef.nodeMarker))

        // Adds the created value to the value store and also to the tree as a child of the parent node
        addTreeNode(
            treeDef = treeDef,
            parentId = parentId,
            child = child
        )
    }
}

//@example
suspend fun linkTreeNodeExample(valueWorker: AvValueWorker, parentId: AvValueId, treeDef: AvTreeDef) {
    valueWorker.execute {

        // Creates a value and adds it to the value store but does not add it to the tree.
        val child = addValue { AvValue(spec = "Child Node", markersOrNull = setOf(treeDef.nodeMarker)) }

        // Links the created value to the tree as a child of the parent node.
        linkTreeNode(
            treeDef = treeDef,
            parentId = parentId,
            childId = child.uuid
        )
    }
}

//@example
suspend fun removeTreeNodeExample(valueWorker: AvValueWorker, parentId: AvValueId, childId: AvValueId, treeDef: AvTreeDef) {
    valueWorker.execute {
        // Removes a child node from its parent in the tree
        removeTreeNode(
            treeDef = treeDef,
            parentId = parentId,
            childId = childId
        )
    }
}

//@example
suspend fun moveTreeNodeUpExample(valueWorker: AvValueWorker, childId: AvValueId, treeDef: AvTreeDef) {
    valueWorker.execute {
        // Moves a node up in the ordering of its siblings in the tree
        moveTreeNodeUp(
            treeDef = treeDef,
            childId = childId
        )
    }
}

//@example
suspend fun moveTreeNodeDownExample(valueWorker: AvValueWorker, childId: AvValueId, treeDef: AvTreeDef) {
    valueWorker.execute {
        // Moves a node down in the ordering of its siblings in the tree
        moveTreeNodeDown(
            treeDef = treeDef,
            childId = childId
        )
    }
}

//@example
suspend fun getTreeChildIdsExample(valueWorker: AvValueWorker, parentId: AvValueId, treeDef: AvTreeDef) {
    valueWorker.execute {
        // Retrieves a list of IDs for all direct children of a parent node
        val children: List<AvValueId> = getTreeChildIds(treeDef, parentId)
        println("Children: $children")
    }
}

//@example
suspend fun getTreeSiblingIdsExample(valueWorker: AvValueWorker, childId: AvValueId, treeDef: AvTreeDef) {
    valueWorker.execute {
        // Retrieves a list of IDs for all siblings of a node (nodes sharing the same parent)
        val siblings: List<AvValueId> = getTreeSiblingIds(treeDef, childId)
        println("Siblings: $siblings")
    }
}



