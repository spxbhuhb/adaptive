package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.IdentityMapping

fun <T> selectInputBackend(
    inputValue: T? = null,
    builder: SingleSelectInputViewBackendBuilder<T,T>.() -> Unit = { }
) =
    SingleSelectInputViewBackendBuilder(inputValue, IdentityMapping()).apply(builder).toBackend()