package `fun`.adaptive.value.store

import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.p04
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue.Companion.withSpec
import `fun`.adaptive.value.model.AvRefLabels
import `fun`.adaptive.value.model.AvTreeSetup
import `fun`.adaptive.value.operation.AvoAdd
import `fun`.adaptive.value.operation.AvoAddOrUpdate

class AvComputeContext(
    val store: AvValueStore,
    val commitSet: MutableSet<AvSubscription>
) {

    operator fun plusAssign(value: AvValue<*>) {
        store.addOrUpdate(AvoAddOrUpdate(value), commitSet)
    }

    inline fun <T> addValue(valueFun : () -> AvValue<T>) = addValue(valueFun())

    fun <T> addValue(value : AvValue<T>) : AvValue<T> {
        store.add(AvoAdd(value), commitSet)
        return value
    }

    inline fun <reified T : Any> get(valueId: AvValueId): AvValue<T> =
        checkNotNull(getOrNull(valueId)) { "cannot find item for id $valueId" }.withSpec<T>()

    fun getOrNull(valueId: AvValueId?): AvValue<*>? =
        store.unsafeGetOrNull(valueId)

    inline fun <reified T : Any> ref(valueId: AvValueId, refMarker: AvMarker): AvValue<T> =
        checkNotNull(refOrNull(valueId, refMarker)) { "cannot find ref item for marker $refMarker in item $valueId" }
            .withSpec<T>()

    fun refOrNull(valueId: AvValueId, refMarker: AvMarker): AvValue<*>? =
        store.unsafeRefOrNull(valueId, refMarker)

    inline fun <reified T : Any> ref(value: AvValue<*>, refMarker: AvMarker): AvValue<T> =
        checkNotNull(refOrNull(value, refMarker)) { "cannot find ref item for marker $refMarker in item ${value.uuid}" }
            .withSpec<T>()

    fun refOrNull(value: AvValue<*>, refMarker: AvMarker): AvValue<*>? =
        store.unsafeRefOrNull(value, refMarker)

    fun nextFriendlyId(marker: AvMarker, prefix: String): String {
        var max = 0

        store.unsafeForEachItemByMarker(marker) { value ->
            val i = value.friendlyId?.removePrefix(prefix)?.toIntOrNull()
            if (i != null && i > max) max = i
        }

        return "$prefix${(max + 1).p04}"
    }

    fun queryByMarker(marker: AvMarker): List<AvValue<*>> =
        store.unsafeQueryByMarker(marker)

    @Suppress("unused") // used for debugging
    fun dump(): String = store.dump()

    //---------------------------------------------------------------------------------
    // Tree operations
    //---------------------------------------------------------------------------------

    fun <T> addTreeNode(
        parentId: AvValueId?,
        treeSetup: AvTreeSetup,
        buildFun : () -> AvValue<T>
    ) : AvValue<T> {
        val value = buildFun()
        this += value
        addTreeNode(parentId, value.uuid, treeSetup)
        return value
    }

    fun addTreeNode(
        parentId: AvValueId?,
        childId: AvValue<*>,
        treeSetup: AvTreeSetup
    ) {
        this += childId
        addTreeNode(parentId, childId, treeSetup)
    }

    /**
     * Adds a child value to a parent value in the tree structure.
     * 
     * If the child list already exists, the child ID is added to it.
     * If not, creates a new child list value and updates parent references.
     *
     * When [parentId] is null and a root list marker is set in the tree setup,
     * the value is added as a root node to the root node list.
     *
     * @param parentId The ID of the parent value, or null for root nodes
     * @param childId The ID of the value to add
     * @param treeSetup The tree setup configuration containing reference labels and markers
     */
    fun addTreeNode(
        parentId: AvValueId?,
        childId: AvValueId,
        treeSetup: AvTreeSetup
    ) {
        check(parentId != childId) { "cannot add a node to itself" }

        if (parentId == null) {
            addRootTreeNode(childId, treeSetup)
            return
        }

        // collect data from the store
        val child = store.unsafeGet(childId)
        val parent = store.unsafeGet(parentId)
        val original: AvValue<AvRefListSpec>? = refOrNull(parent, treeSetup.childListRefLabel)?.withSpec()

        // update child-to-parent reference
        val childRefs = child.mutableRefs()
        childRefs[treeSetup.parentRefLabel] = parent.uuid

        this += child.copy(refsOrNull = childRefs)

        // if there is a list already, update it
        if (original != null) {
            if (childId in original.spec.refs) return
            this += original.copy(spec = AvRefListSpec(original.spec.refs + childId))
            return
        }

        // there is no list yet, create a new one and update parent-to-list reference
        val new = AvValue(
            uuid = uuid7(),
            markersOrNull = setOf(treeSetup.childListMarker),
            refsOrNull = mapOf(AvRefLabels.REF_LIST_OWNER to parentId),
            spec = AvRefListSpec(listOf(childId))
        )

        val parentRefs = parent.mutableRefs()
        parentRefs[treeSetup.childListRefLabel] = new.uuid

        this += new
        this += parent.copy(refsOrNull = parentRefs)
    }

    private fun addRootTreeNode(
        nodeId: AvValueId,
        treeSetup: AvTreeSetup
    ) {
        if (treeSetup.rootListMarker == null) return

        val rootList = queryByMarker(treeSetup.rootListMarker).firstOrNull()?.withSpec<AvRefListSpec>()

        if (rootList != null) {
            if (nodeId in rootList.spec.refs) return
            this += rootList.copy(spec = AvRefListSpec(rootList.spec.refs + nodeId))
            return
        }

        val new = AvValue(
            uuid = uuid7(),
            markersOrNull = setOf(treeSetup.rootListMarker),
            spec = AvRefListSpec(listOf(nodeId))
        )

        this += new
    }

    /**
     * Removes a child value from its parent's child list in the tree structure.
     *
     * @param parentId The ID of the parent value
     * @param childId The ID of the child value to remove
     * 
     * @param treeSetup The tree setup configuration containing reference labels and markers
     */
    fun removeTreeNode(
        parentId: AvValueId?,
        childId: AvValueId,
        treeSetup: AvTreeSetup
    ) {
        if (parentId == null) {
            removeRootTreeNode(childId, treeSetup)
            return
        }
        
        val original: AvValue<AvRefListSpec>? = refOrNull(parentId, treeSetup.childListRefLabel)?.withSpec()

        if (original != null) {
            this += original.copy(spec = AvRefListSpec(original.spec.refs - childId))
        }
    }

    private fun removeRootTreeNode(
        nodeId: AvValueId,
        treeSetup: AvTreeSetup
    ) {
        if (treeSetup.rootListMarker == null) return

        val rootList = queryByMarker(treeSetup.rootListMarker).firstOrNull()?.withSpec<AvRefListSpec>()

        if (rootList != null) {
            this += rootList.copy(spec = AvRefListSpec(rootList.spec.refs - nodeId))
        }
    }

    /**
     * Moves a child value one position up in its parent's child list.
     * If the child is already at the top, no changes are made.
     *
     * @param childId The ID of the child to move up
     * @param treeSetup The tree setup configuration containing reference labels and markers
     */
    fun moveTreeNodeUp(
        childId: AvValueId,
        treeSetup: AvTreeSetup
    ) {
        val child = get<Any>(childId)
        val parent = ref<Any>(child, treeSetup.parentRefLabel)
        val original = ref<AvRefListSpec>(parent, treeSetup.childListRefLabel)

        val originalList = original.spec.refs.toMutableList()
        val index = originalList.indexOf(childId)
        if (index < 1) return

        val newList = originalList.toMutableList()
        newList[index] = newList[index - 1]
        newList[index - 1] = childId

        this += original.copy(spec = AvRefListSpec(newList))
    }

    /**
     * Moves a child value one position down in its parent's child list.
     * If the child is already at the bottom, no changes are made.
     *
     * @param childId The ID of the child to move down
     * @param treeSetup The tree setup configuration containing reference labels and markers
     */
    fun moveTreeNodeDown(
        childId: AvValueId,
        treeSetup: AvTreeSetup
    ) {
        val child = get<Any>(childId)
        val parent = ref<Any>(child, treeSetup.parentRefLabel)
        val original = ref<AvRefListSpec>(parent, treeSetup.childListRefLabel)

        val originalList = original.spec.refs.toMutableList()
        val index = originalList.indexOf(childId)
        if (index >= originalList.lastIndex) return

        val newList = originalList.toMutableList()
        newList[index] = newList[index + 1]
        newList[index + 1] = childId

        this += original.copy(spec = AvRefListSpec(newList))
    }

    /**
     * Returns a list of child value IDs for the given parent ID in the tree structure.
     * If the parent has no children or the child list reference doesn't exist, it returns an empty list.
     *
     * @param parentId The ID of the parent node to get children for
     * @param setup The tree setup configuration containing reference labels and markers
     *
     * @return List of value IDs representing the children of the given parent
     */
    fun getTreeChildIds(
        parentId: AvValueId,
        setup : AvTreeSetup
    ): List<AvValueId> {
        val listValue = store.unsafeRefOrNull(parentId, setup.childListRefLabel) ?: return emptyList()
        return (listValue.spec as AvRefListSpec).refs
    }
    
    /**
     * Returns a list of sibling value IDs for the given child ID in the tree structure.
     * If the child has no parent and no root list marker is defined, returns an empty list.
     *
     * @param childId The ID of the value to find siblings for
     * @param setup The tree setup configuration containing reference labels and markers
     *
     * @return List of value IDs representing the siblings of the given child (including the child)
     */
    fun getTreeSiblingIds(
        childId: AvValueId,
        setup : AvTreeSetup
    ): List<AvValueId> {
        val parentId = store.unsafeGet(childId).refIdOrNull(setup.parentRefLabel)

        val listValue: AvValue<*>?

        if (parentId == null) {
            if (setup.rootListMarker == null) return emptyList()
            listValue = queryByMarker(setup.rootListMarker).firstOrNull()
        } else {
            listValue = store.unsafeRefOrNull(parentId, setup.childListRefLabel)
        }

        return listValue?.spec?.let { (it as AvRefListSpec).refs } ?: emptyList()
    }

}