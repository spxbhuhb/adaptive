package `fun`.adaptive.ui.input.badge

fun badgeInputBackend(inputValue : Set<String>? = null, builder: BadgeInputViewBackendBuilder.() -> Unit = {  }) =
    BadgeInputViewBackendBuilder(inputValue).apply(builder).toBackend()
