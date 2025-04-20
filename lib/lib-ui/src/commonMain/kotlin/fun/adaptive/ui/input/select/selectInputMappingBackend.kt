package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping

fun <VT,OT> selectInputMappingBackend(
    inputValue: VT? = null,
    mapping : SelectOptionMapping<VT,OT>,
    builder: SingleSelectInputViewBackendBuilder<VT,OT>.() -> Unit = { }
) =
    SingleSelectInputViewBackendBuilder(inputValue, mapping).apply(builder).toBackend()