package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.IdentityOptionMapping

fun <T> selectInputBackend(
    inputValue: T? = null,
    builder: SingleSelectInputViewBackendBuilder<T,T>.() -> Unit = { }
) =
    SingleSelectInputViewBackendBuilder(inputValue, IdentityOptionMapping()).apply(builder).toBackend()