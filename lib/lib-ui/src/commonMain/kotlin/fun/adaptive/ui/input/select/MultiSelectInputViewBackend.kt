package `fun`.adaptive.ui.input.select

class MultiSelectInputViewBackend<VT>(
    value: Set<VT>? = null,
    label: String? = null,
    isSecret: Boolean = false
) : AbstractSelectInputViewBackend<Set<VT>, VT>(
    value, label, isSecret
) {
    init {
        if (value != null) {
            selectedOptions += value
        }
    }

    override fun updateInputValue(oldValue: Set<VT>) {
        inputValue = selectedOptions
    }

}
