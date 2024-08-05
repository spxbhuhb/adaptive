package hu.simplexion.adaptive.auto.deprecated.state

import hu.simplexion.adaptive.auto.FragmentStore
import hu.simplexion.adaptive.auto.ItemId

class StateData(
    val store: FragmentStore
) {

    private val states = mutableMapOf<ItemId, Array<StateOperation>>()

    fun afterApply(op: StateOperation) {
        val state = states[op.fragmentId] ?: return // TODO what to do when the fragment is missing
        if (op.timestamp > state[op.propertyIndex].timestamp) {
            state[op.propertyIndex] = op
            store.stateChange(op)
        }
    }

}