package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.IdentityMapping

fun <T> multiSelectInputBackend(
    inputValue: Set<T>? = null,
    builder: MultiSelectInputViewBackendBuilder<T,T>.() -> Unit = { }
) =
    MultiSelectInputViewBackendBuilder(inputValue, IdentityMapping()).apply(builder).toBackend()