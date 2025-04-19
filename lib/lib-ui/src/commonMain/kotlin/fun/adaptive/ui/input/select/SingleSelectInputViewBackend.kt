package `fun`.adaptive.ui.input.select

class SingleSelectInputViewBackend<VT>(
    value: VT? = null,
    label: String? = null,
    isSecret: Boolean = false
) : AbstractSelectInputViewBackend<VT, VT>(
    value, label, isSecret
) {
    init {
        if (value != null) {
            selectedOptions += value
        }
    }

    override fun updateInputValue(oldValue: Set<VT>) {
        inputValue = selectedOptions.firstOrNull()
    }
}
