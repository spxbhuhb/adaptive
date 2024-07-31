package hu.simplexion.adaptive.auto

import hu.simplexion.adaptive.auto.state.StateData
import hu.simplexion.adaptive.auto.state.StateOperation
import hu.simplexion.adaptive.auto.tree.TreeData
import hu.simplexion.adaptive.auto.tree.TreeOperation
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.utility.UUID

class FragmentStore(
    val globalStoreId: UUID<FragmentStore>,
    timestamp: Int,
    instanceId: Int
) {

    private var time = LamportTimestamp(timestamp, instanceId)

    private val tree = TreeData(this)
    private val state = StateData(this)

    private val fragments = mutableMapOf<ItemId, AdaptiveFragment>()

    // --------------------------------------------------------------------------------
    // Public functions to traverse the tree, add/move/modify/remove fragments
    // --------------------------------------------------------------------------------

    fun add(fragment: AdaptiveFragment, parent: AdaptiveFragment) {
        // TODO time = tick()
        // TODO add to fragments
        // TODO add to tree
        // TODO copy state into states
        // TODO set fragment parent
        // TODO add to the children of parent
        // TODO mount
        // TODO send operation to peers: FragmentOperation(fragmentId, parentId, fragmentType, initialState)
    }

    fun move(fragment: AdaptiveFragment, newParent: AdaptiveFragment) {
        // TODO time = tick()
        // TODO operation = TreeOperation(fragmentId, parentId, largestCounter, time)
        // TODO tree.afterApply(operation)
        // TODO fragment.switchParent(newParent)
        // TODO send operation to peers
    }

    fun modify(fragment: AdaptiveFragment, propertyIndex: Int, propertyValue: Any?) {
        // TODO time = tick()
        // TODO val operation = StateOperation(fragmentId, propertyIndex, propertyValue, time)
        // TODO state.afterApply(operation)
        // TODO send operation to peers
    }

    fun remove(fragment: AdaptiveFragment) {
        // TODO time = tick()
        // TODO operation = TreeOperation(fragmentId, FragmentId(0, 2), largestCounter, time)
        // TODO tree.afterApply(operation)
        // TODO fragment.detach() ??? unmount, dispose?, parent = null
        // TODO send operation to peers
    }

    // --------------------------------------------------------------------------------
    // Internal functions
    // --------------------------------------------------------------------------------

    fun add(op: TreeOperation) {
        time = time.receive(op.timestamp)
        tree.afterApply(op)
    }

    fun add(op: StateOperation) {
        time = time.receive(op.timestamp)
        state.afterApply(op)
    }

    fun nextTime(): LamportTimestamp {
        TODO()
    }

    fun treeChange(op: TreeOperation) {

    }

    fun stateChange(op: StateOperation) {
        val fragment = fragments[op.fragmentId] ?: return // TODO what to do on missing fragment?
        fragment.setStateVariable(op.propertyIndex, op.propertyValue)
    }

}