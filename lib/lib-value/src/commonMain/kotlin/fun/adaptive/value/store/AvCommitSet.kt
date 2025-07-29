package `fun`.adaptive.value.store

import `fun`.adaptive.value.AvSubscription
import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId

class AvCommitSet(
    val subscriptions: MutableSet<AvSubscription> = mutableSetOf(),
    val values: MutableMap<AvValueId, AvValue<*>> = mutableMapOf(),
    val removedValues: MutableSet<AvValueId> = mutableSetOf()
) {
    fun clear() {
        subscriptions.clear()
        values.clear()
        removedValues.clear()
    }
}