package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping

class SingleSelectInputViewBackendBuilder<VT, OT>(
    inputValue: VT?,
    var mapping: SelectOptionMapping<VT, OT>,
) : AbstractSelectInputViewBackendBuilder<VT, VT, OT>(inputValue) {

    override fun toBackend() =
        SingleSelectInputViewBackend(inputValue, mapping, label, secret).also {
            setup(it as AbstractSelectInputViewBackend<VT, VT, OT>)
        }

}