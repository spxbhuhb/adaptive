package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.SelectInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping

class SingleSelectInputViewBackend<INPUT_VALUE_TYPE,ITEM_TYPE,OPTION_TYPE>(
    value: INPUT_VALUE_TYPE? = null,
    optionToItemMapping : SelectOptionMapping<ITEM_TYPE, OPTION_TYPE>,
    inputToItemMapping: SelectInputMapping<INPUT_VALUE_TYPE, ITEM_TYPE>,
    label: String? = null,
    isSecret: Boolean = false
) : AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, ITEM_TYPE, OPTION_TYPE>(
    value, optionToItemMapping, inputToItemMapping, label, isSecret
) {
    init {
        if (value != null) {
            inputToItemMapping.valueToItem(value)?.let { selectedValues += it }
        }
    }

    override fun updateInputValue(oldValue: INPUT_VALUE_TYPE?) {
        inputValue = inputToItemMapping.itemToValue(this, selectedValues.firstOrNull())
    }
}
