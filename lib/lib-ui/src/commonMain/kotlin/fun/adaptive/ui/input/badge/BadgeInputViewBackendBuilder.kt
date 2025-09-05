package `fun`.adaptive.ui.input.badge

import `fun`.adaptive.ui.input.InputViewBackendBuilder

class BadgeInputViewBackendBuilder(
    inputValue: Set<String>?
) : InputViewBackendBuilder<Set<String>, BadgeInputViewBackend>(inputValue) {

    var badgeInputTheme : BadgeInputTheme? = null
    var unremovable : Set<String>? = null

    override fun toBackend() =
        BadgeInputViewBackend(inputValue, label).also { backend ->
            setup(backend)
            badgeInputTheme?.let { backend.badgeInputTheme = it }
            unremovable?.let { backend.unremovable = it }
            backend.localValidate()
        }

}