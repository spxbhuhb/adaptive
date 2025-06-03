package `fun`.adaptive.value.store

import `fun`.adaptive.utility.UUID.Companion.uuid7
import `fun`.adaptive.utility.p04
import `fun`.adaptive.value.*
import `fun`.adaptive.value.AvValue.Companion.checkSpec
import `fun`.adaptive.value.model.AvRefLabels
import `fun`.adaptive.value.model.AvTreeDef
import `fun`.adaptive.value.operation.AvoAdd
import `fun`.adaptive.value.operation.AvoAddOrUpdate

class AvComputeContext(
    val store: AvValueStore,
    val commitSet: MutableSet<AvSubscription>
) {

    operator fun plusAssign(value: AvValue<*>) {
        store.addOrUpdate(AvoAddOrUpdate(value), commitSet)
    }

    /**
     * Adds a value to the value store.
     *
     * @param valueFun A function that returns the value to add.
     * @return The added value.
     * @throws IllegalStateException if the value is already in the store.
     */
    inline fun <T> addValue(valueFun: () -> AvValue<T>) = addValue(valueFun())

    /**
     * Adds a value to the value store.
     *
     * @param value The value to add.
     * @return The added value.
     * @throws IllegalStateException if the value is already in the store.
     */
    fun <T> addValue(value: AvValue<T>): AvValue<T> {
        store.add(AvoAdd(value), commitSet)
        return value
    }

    /**
     * Gets a value from the store with the specified ID and type.
     *
     * @param valueId The ID of the value to retrieve
     * @return The value cast to the specified type T
     * @throws NoSuchElementException if no value exists with the given ID
     * @throws IllegalStateException if the `spec` of the retrieved value is not of type [T]
     */
    inline fun <reified T : Any> get(valueId: AvValueId): AvValue<T> =
        getOrNullGen(valueId)?.checkSpec<T>()
            ?: throw NoSuchElementException("cannot find item for id $valueId")

    /**
     * Gets a value from the store with the specified ID, or null if it doesn't exist.
     *
     * @param valueId The ID of the value to retrieve
     * @return The value if found, null otherwise
     */
    inline fun <reified T : Any> getOrNull(valueId: AvValueId?): AvValue<T>? =
        getOrNullGen(valueId)?.checkSpec<T>()

    /**
     * Gets a generic (without `spec` check) value from the store with the specified ID, or null if it doesn't exist.
     *
     * @param valueId The ID of the value to retrieve
     * @return The value if found, null otherwise
     */
    fun getOrNullGen(valueId: AvValueId?): AvValue<*>? =
        store.unsafeGetOrNull(valueId)

    //---------------------------------------------------------------------------------
    // Status
    //---------------------------------------------------------------------------------

    fun addStatus(valueId : AvValueId, status : AvStatus) {
        val value = store.unsafeGet(valueId)
        this += value.copy(statusOrNull = value.mutableStatus().also { it += status })
    }

    fun addStatus(valueId : AvValueId, vararg statuses : AvStatus) {
        val value = store.unsafeGet(valueId)
        this += value.copy(statusOrNull = value.mutableStatus().also { it.addAll(statuses) })
    }

    fun removeStatus(valueId : AvValueId, status : AvStatus) {
        val value = store.unsafeGet(valueId)
        this += value.copy(statusOrNull = value.mutableStatus().also { it -= status })
    }

    fun removeStatus(valueId : AvValueId, vararg statuses : AvStatus) {
        val value = store.unsafeGet(valueId)
        this += value.copy(statusOrNull = value.mutableStatus().also { it.removeAll(statuses) })
    }

    //---------------------------------------------------------------------------------
    // Markers
    //---------------------------------------------------------------------------------

    fun addMarker(valueId : AvValueId, marker : AvMarker) {
        val value = store.unsafeGet(valueId)
        if (value.markersOrNull?.contains(marker) == true) return
        this += value.copy(markersOrNull = value.mutableMarkers().also { it += marker })
    }

    fun addMarker(valueId : AvValueId, vararg markers : AvMarker) {
        val value = store.unsafeGet(valueId)
        if (value.markersOrNull?.containsAll(markers.toList()) == true) return
        this += value.copy(markersOrNull = value.mutableMarkers().also { it.addAll(markers) })
    }

    fun removeMarker(valueId : AvValueId, marker : AvMarker) {
        val value = store.unsafeGet(valueId)
        if (value.markersOrNull?.contains(marker) != true) return
        this += value.copy(markersOrNull = value.mutableMarkers().also { it -= marker })
    }

    fun removedMarker(valueId : AvValueId, vararg markers : AvMarker) {
        val value = store.unsafeGet(valueId)
        if (value.markersOrNull?.containsAll(markers.toList()) != true) return
        this += value.copy(markersOrNull = value.mutableMarkers().also { it.removeAll(markers) })
    }

    //---------------------------------------------------------------------------------
    // Reference handling
    //---------------------------------------------------------------------------------

    /**
     * Gets a referred value from the store based on the id of the
     * referring value and a reference label.
     *
     * @param valueId The ID of the value containing the reference
     * @param refLabel The reference label identifying the reference
     * @return The referred value cast to the specified type T
     * @throws NoSuchElementException if there is no [refLabel] in `refsOrNull` of if
     *                                there is but the referred value is not in the store
     * @throws IllegalStateException if the `spec` of the referred value is not of type [T]
     */
    inline fun <reified T : Any> ref(valueId: AvValueId, refLabel: AvRefLabel): AvValue<T> =
        refOrNullGen(valueId, refLabel)?.checkSpec<T>()
            ?: throw NoSuchElementException("cannot find ref item for marker $refLabel in item $valueId")

    /**
     * Gets a referred value from the store based on the id of the
     * referring value and a reference label, or null if it doesn't exist.
     *
     * @param valueId The ID of the value containing the reference
     * @param refLabel The reference label identifying the reference
     * @return The referred value if found, null otherwise
     */
    inline fun <reified T : Any> refOrNull(valueId: AvValueId, refLabel: AvRefLabel): AvValue<T>? =
        refOrNullGen(valueId, refLabel)?.checkSpec<T>()

    /**
     * Gets a generic (without `spec` check) referred value from the store based on the id of the
     * referring value and a reference label, or null if it doesn't exist.
     *
     * @param valueId The ID of the value containing the reference
     * @param refLabel The reference label identifying the reference
     * @return The referred value if found, null otherwise
     */
    fun refOrNullGen(valueId: AvValueId, refLabel: AvRefLabel): AvValue<*>? =
        store.unsafeRefOrNull(valueId, refLabel)

    /**
     * Gets a referred value from the store based on the id of the
     * referring value and a reference label.
     *
     * @param value The value containing the reference
     * @param refLabel The reference label identifying the reference
     * @return The referred value cast to the specified type T
     * @throws NoSuchElementException if there is no [refLabel] in `refsOrNull` of if
     *                                there is but the referred value is not in the store
     * @throws IllegalStateException if the `spec` of the referred value is not of type [T]
     */
    inline fun <reified T : Any> ref(value: AvValue<*>, refLabel: AvRefLabel): AvValue<T> =
        checkNotNull(refOrNullGen(value, refLabel)) { "cannot find ref item for marker $refLabel in item ${value.uuid}" }
            .checkSpec<T>()

    /**
     * Gets a referred value from the store based on the id of the
     * referring value and a reference label, or null if it doesn't exist.
     *
     * @param value The value containing the reference
     * @param refLabel The reference label identifying the reference
     * @return The referred value if found, null otherwise
     */
    inline fun <reified T : Any> refOrNull(value: AvValue<*>, refLabel: AvRefLabel): AvValue<T>? =
        refOrNullGen(value, refLabel)?.checkSpec<T>()

    /**
     * Gets a generic (without `spec` check) referred value from the store based on the id of the
     * referring value and a reference label, or null if it doesn't exist.
     *
     * @param value The value containing the reference
     * @param refLabel The reference label identifying the reference
     * @return The referred value if found, null otherwise
     */
    fun refOrNullGen(value: AvValue<*>, refLabel: AvRefLabel): AvValue<*>? =
        store.unsafeRefOrNull(value, refLabel)

    /**
     * Adds a reference from one value to another value in the store.
     * Updates the referring value by adding the reference to its refsOrNull map.
     *
     * @param referringId The ID of the value that will contain the reference
     * @param referredId The ID of the value being referenced
     * @param refLabel The label used to identify this reference
     */
    fun addRef(referringId: AvValueId, referredId: AvValueId, refLabel: AvRefLabel) =
        addRef(store.unsafeGet(referringId), referredId, refLabel)

    /**
     * Adds a reference from one value to another value in the store.
     * Updates the referring value by adding the reference to its refsOrNull map.
     *
     * @param referring The value that will contain the reference
     * @param referredId The ID of the value being referenced
     * @param refLabel The label used to identify this reference
     */
    fun addRef(referring: AvValue<*>, referredId: AvValueId, refLabel: AvRefLabel) {
        val refs = referring.mutableRefs()
        refs[refLabel] = referredId
        this += referring.copy(refsOrNull = refs)
    }

    /**
     * Removes a reference from a value in the store identified by the reference label.
     *
     * **DOES NOT REMOVE THE REFERRED VALUE!**
     *
     * @param referringId The ID of the value containing the reference to remove
     * @param refLabel The label identifying the reference to remove
     */
    fun removeRef(referringId: AvValueId, refLabel: AvRefLabel) {
        val referring = store.unsafeGet(referringId)
        val refs = referring.mutableRefs()
        refs.remove(refLabel)
        this += referring.copy(refsOrNull = refs)
    }

    //---------------------------------------------------------------------------------
    // Reference list handling
    //---------------------------------------------------------------------------------

    inline fun <reified SPEC : Any> refListOrNull(valueId: AvValueId, refLabel: AvRefLabel): List<AvValue<SPEC>>? =
        refListOrNullGen(valueId, refLabel)?.map { it.checkSpec(SPEC::class) }

    fun refListOrNullGen(valueId: AvValueId, refLabel: AvRefLabel) : List<AvValue<*>>? =
        store.unsafeRefListOrNull(store.unsafeGet(valueId), refLabel)

    //---------------------------------------------------------------------------------
    // Tree operations
    //---------------------------------------------------------------------------------

    /**
     * Adds a new value to the store and adds it as a child node in the tree structure.
     * The value is created using the provided build function.
     *
     * When [parentId] is null and a root list marker is set in the tree definition,
     * the value is added as a root node to the root node list.
     *
     * @param parentId The ID of the parent value, or null for root nodes
     * @param treeDef The tree definition containing reference labels and markers
     * @param buildFun A function that creates the value to add
     * @return The newly created and added value
     */
    fun <T> addTreeNode(
        treeDef: AvTreeDef,
        parentId: AvValueId? = null,
        buildFun: () -> AvValue<T>
    ): AvValue<T> {
        val value = buildFun()
        this += value
        linkTreeNode(treeDef, parentId, value)
        return value
    }

    /**
     * Adds an existing value as a child node in the tree structure.
     *
     * When [parentId] is null and a root list marker is set in the tree definition,
     * the value is added as a root node to the root node list.
     *
     * @param parentId The ID of the parent value, or null for root nodes
     * @param child The value to add as a child node
     * @param treeDef The tree definition containing reference labels and markers
     */
    fun addTreeNode(
        treeDef: AvTreeDef,
        parentId: AvValueId? = null,
        child: AvValue<*>
    ) {
        addTreeNode(treeDef, parentId, child)
    }

    /**
     * Links a child value to a parent value in the tree structure.
     *
     * If the list of children already exists, the child ID is added to it.
     * If not, creates a new list of children value and updates parent references.
     *
     * When [parentId] is null and a root list marker is set in the tree definition,
     * the value is added as a root node to the root node list.
     *
     * @param parentId The ID of the parent value, or null for root nodes
     * @param childId The ID of the value to add
     * @param treeDef The tree definition containing reference labels and markers
     */
    fun linkTreeNode(
        treeDef: AvTreeDef,
        parentId: AvValueId?,
        childId: AvValueId
    ) {
        linkTreeNode(treeDef, parentId, store.unsafeGet(childId))
    }

    /**
     * Links a child value to a parent value in the tree structure.
     *
     * If the list of children already exists, the child ID is added to it.
     * If not, creates a new list of children value and updates parent references.
     *
     * When [parentId] is null and a root list marker is set in the tree definition,
     * the value is added as a root node to the root node list.
     *
     * @param parentId The ID of the parent value, or null for root nodes
     * @param child The value to add
     * @param treeDef The tree definition containing reference labels and markers
     */
    fun linkTreeNode(
        treeDef: AvTreeDef,
        parentId: AvValueId?,
        child: AvValue<*>
    ) {
        val childId = child.uuid
        check(parentId != childId) { "cannot add a node to itself" }

        if (parentId == null) {
            addRootTreeNode(treeDef, child)
            return
        }

        // collect data from the store
        val parent = store.unsafeGet(parentId)
        val original = refOrNull<AvRefListSpec>(parent, treeDef.childListRefLabel)

        // update child-to-parent reference
        val childRefs = child.mutableRefs()
        childRefs[treeDef.parentRefLabel] = parent.uuid

        if (child.markersOrNull?.contains(treeDef.nodeMarker) != true) {
            this += child.copy(
                markersOrNull = child.mutableMarkers().also { it += treeDef.nodeMarker },
                refsOrNull = childRefs
            )
        } else {
            this += child.copy(refsOrNull = childRefs)
        }

        // if there is a list already, update it
        if (original != null) {
            if (childId in original.spec.refs) return
            this += original.copy(spec = AvRefListSpec(original.spec.refs + childId))
            return
        }

        // there is no list yet, create a new one and update parent-to-list reference
        val new = AvValue(
            uuid = uuid7(),
            markersOrNull = setOf(treeDef.childListMarker),
            refsOrNull = mapOf(AvRefLabels.REF_LIST_OWNER to parentId),
            spec = AvRefListSpec(listOf(childId))
        )

        val parentRefs = parent.mutableRefs()
        parentRefs[treeDef.childListRefLabel] = new.uuid

        this += new
        this += parent.copy(refsOrNull = parentRefs)
    }

    private fun addRootTreeNode(
        treeDef: AvTreeDef,
        node: AvValue<*>
    ) {
        if (treeDef.rootListMarker == null) return

        if (node.markersOrNull?.contains(treeDef.nodeMarker) != true) {
            this += node.copy(markersOrNull = node.mutableMarkers().also { it += treeDef.nodeMarker })
        }

        val nodeId = node.uuid

        val rootList = queryByMarker(treeDef.rootListMarker).firstOrNull()?.checkSpec<AvRefListSpec>()

        if (rootList != null) {
            if (nodeId in rootList.spec.refs) return
            this += rootList.copy(spec = AvRefListSpec(rootList.spec.refs + nodeId))
            return
        }

        val new = AvValue(
            uuid = uuid7(),
            markersOrNull = setOf(treeDef.rootListMarker),
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
     * @param treeDef The tree definition containing reference labels and markers
     */
    fun removeTreeNode(
        treeDef: AvTreeDef,
        parentId: AvValueId?,
        childId: AvValueId
    ) {
        if (parentId == null) {
            removeRootTreeNode(treeDef, childId)
            return
        }

        val original = refOrNull<AvRefListSpec>(parentId, treeDef.childListRefLabel)

        if (original != null) {
            this += original.copy(spec = AvRefListSpec(original.spec.refs - childId))
        }
    }

    private fun removeRootTreeNode(
        treeDef: AvTreeDef,
        nodeId: AvValueId
    ) {
        if (treeDef.rootListMarker == null) return

        val rootList = queryByMarker(treeDef.rootListMarker).firstOrNull()?.checkSpec<AvRefListSpec>()

        if (rootList != null) {
            this += rootList.copy(spec = AvRefListSpec(rootList.spec.refs - nodeId))
        }
    }

    /**
     * Moves a child value one position up in its parent's child list.
     * If the child is already at the top, no changes are made.
     *
     * @param childId The ID of the child to move up
     * @param treeDef The tree definition containing reference labels and markers
     */
    fun moveTreeNodeUp(
        treeDef: AvTreeDef,
        childId: AvValueId
    ) {
        val child = get<Any>(childId)
        val parent = ref<Any>(child, treeDef.parentRefLabel)
        val original = ref<AvRefListSpec>(parent, treeDef.childListRefLabel)

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
     * @param treeDef The tree definition containing reference labels and markers
     */
    fun moveTreeNodeDown(
        treeDef: AvTreeDef,
        childId: AvValueId
    ) {
        val child = get<Any>(childId)
        val parent = ref<Any>(child, treeDef.parentRefLabel)
        val original = ref<AvRefListSpec>(parent, treeDef.childListRefLabel)

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
     * @param treeDef The tree definition containing reference labels and markers
     *
     * @return List of value IDs representing the children of the given parent
     */
    fun getTreeChildIds(
        treeDef: AvTreeDef,
        parentId: AvValueId
    ): List<AvValueId> {
        val listValue = store.unsafeRefOrNull(parentId, treeDef.childListRefLabel) ?: return emptyList()
        return (listValue.spec as AvRefListSpec).refs
    }

    /**
     * Returns a list of sibling value IDs for the given child ID in the tree structure.
     * If the child has no parent and no root list marker is defined, returns an empty list.
     *
     * @param childId The ID of the value to find siblings for
     * @param treeDef The tree definition containing reference labels and markers
     *
     * @return List of value IDs representing the siblings of the given child (including the child)
     */
    fun getTreeSiblingIds(
        treeDef: AvTreeDef,
        childId: AvValueId
    ): List<AvValueId> {
        val parentId = store.unsafeGet(childId).refIdOrNull(treeDef.parentRefLabel)

        val listValue: AvValue<*>?

        if (parentId == null) {
            if (treeDef.rootListMarker == null) return emptyList()
            listValue = queryByMarker(treeDef.rootListMarker).firstOrNull()
        } else {
            listValue = store.unsafeRefOrNull(parentId, treeDef.childListRefLabel)
        }

        return listValue?.spec?.let { (it as AvRefListSpec).refs } ?: emptyList()
    }

    //---------------------------------------------------------------------------------
    // Utility, convenience
    //---------------------------------------------------------------------------------

    /**
     * Generates the next available friendly ID for items with the specified marker.
     * The friendly ID consists of a prefix followed by a 4-digit number padded with zeros.
     * The number is determined by finding the highest existing number for the given prefix
     * and adding 1 to it.
     *
     * @param marker The marker used to filter items to check for existing friendly IDs
     * @param prefix The string prefix to use for the friendly ID
     * @return A new friendly ID string in the format "prefix####" where #### is the next available number
     */
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

}
