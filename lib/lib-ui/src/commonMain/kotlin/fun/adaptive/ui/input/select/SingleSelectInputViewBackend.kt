package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping

class SingleSelectInputViewBackend<IVT,OT>(
    value: IVT? = null,
    mapping : SelectOptionMapping<IVT, OT>,
    label: String? = null,
    isSecret: Boolean = false
) : AbstractSelectInputViewBackend<IVT, IVT, OT>(
    value, mapping, label, isSecret
) {
    init {
        if (value != null) {
            selectedValues += value
        }
    }

    override fun updateInputValue(oldValue: Set<IVT>) {
        inputValue = selectedValues.firstOrNull()
    }
}
