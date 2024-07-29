package hu.simplexion.adaptive.crdt.state

import hu.simplexion.adaptive.crdt.FragmentId
import hu.simplexion.adaptive.crdt.FragmentStore

class StateData(
    val store: FragmentStore
) {

    private val states = mutableMapOf<FragmentId, Array<StateOperation>>()

    fun afterApply(op: StateOperation) {
        val state = states[op.fragmentId] ?: return // TODO what to do when the fragment is missing
        if (op.timestamp > state[op.propertyIndex].timestamp) {
            state[op.propertyIndex] = op
            store.stateChange(op)
        }
    }

}