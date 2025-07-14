package `fun`.adaptive.ui.input.select.mapping

import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend
import `fun`.adaptive.value.AvRefLabel
import `fun`.adaptive.value.AvValueId

/**
 * Maps between `Map<AvRefLabel,AvValueId>` and `AvValueId` for selects.
 */
class RefMapInputMapping(
    val refLabel: AvRefLabel
) : SelectInputMapping<Map<AvRefLabel, AvValueId>, AvValueId> {

    override fun valueToItem(inputValue: Map<AvRefLabel, AvValueId>?): AvValueId? {
        return inputValue?.get(refLabel)
    }

    override fun itemToValue(
        backend: AbstractSelectInputViewBackend<Map<AvRefLabel, AvValueId>, AvValueId, *>,
        item: AvValueId?
    ): Map<AvRefLabel, AvValueId>? {
        if (item == null) {
            return backend.inputValue?.toMutableMap()?.apply { remove(refLabel) }
        } else {
            return backend.inputValue?.toMutableMap()?.apply { put(refLabel, item) }
                ?: mapOf(refLabel to item)
        }
    }

}