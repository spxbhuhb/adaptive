package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.SelectInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping

class MultiSelectInputViewBackend<ITEM_TYPE, OPTION_TYPE>(
    value: Set<ITEM_TYPE>? = null,
    optionToItemMapping: SelectOptionMapping<ITEM_TYPE, OPTION_TYPE>,
    inputToItemMapping: SelectInputMapping<Set<ITEM_TYPE>, ITEM_TYPE>,
    label: String? = null,
    isSecret: Boolean = false
) : AbstractSelectInputViewBackend<Set<ITEM_TYPE>, ITEM_TYPE, OPTION_TYPE>(
    value, optionToItemMapping, inputToItemMapping, label, isSecret
) {
    init {
        if (value != null) {
            selectedValues += value
        }
    }

    override fun updateInputValue(oldValue: Set<ITEM_TYPE>?) {
        if (oldValue == selectedValues) return
        inputValue = selectedValues
    }

}
