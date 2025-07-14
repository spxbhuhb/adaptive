package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.IdentityOptionMapping

fun <T> multiSelectInputBackend(
    inputValue: Set<T>? = null,
    builder: MultiSelectInputViewBackendBuilder<T,T>.() -> Unit = { }
) =
    MultiSelectInputViewBackendBuilder(inputValue, IdentityOptionMapping()).apply(builder).toBackend()