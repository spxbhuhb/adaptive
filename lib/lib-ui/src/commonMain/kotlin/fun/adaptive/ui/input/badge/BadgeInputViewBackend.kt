package `fun`.adaptive.ui.input.badge

import `fun`.adaptive.ui.input.InputViewBackend

class BadgeInputViewBackend(
    value: Set<String>? = null,
    label: String? = null,
) : InputViewBackend<Set<String>, BadgeInputViewBackend>(
    value, label, false
) {

    var badgeInputTheme = BadgeInputTheme.default
    var unremovable = setOf<String>()

    fun addBadge(label: String) {
        if (label.isBlank()) return
        inputValue = inputValue?.let { it + label } ?: setOf(label)
    }

    fun removeBadge(label: String) {
        inputValue = inputValue?.let { it - label } ?: emptySet()
    }

}