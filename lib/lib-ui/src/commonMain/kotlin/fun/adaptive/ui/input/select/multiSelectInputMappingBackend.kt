package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping

fun <VT,OT> multiSelectInputMappingBackend(
    inputValue: Set<VT>? = null,
    mapping : SelectOptionMapping<VT,OT>,
    builder: MultiSelectInputViewBackendBuilder<VT,OT>.() -> Unit = { }
) =
    MultiSelectInputViewBackendBuilder(inputValue, mapping).apply(builder).toBackend()