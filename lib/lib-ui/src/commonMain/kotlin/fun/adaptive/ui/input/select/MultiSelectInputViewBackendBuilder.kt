package `fun`.adaptive.ui.input.select

import `fun`.adaptive.ui.input.select.mapping.IdentityMultiInputMapping
import `fun`.adaptive.ui.input.select.mapping.SelectOptionMapping

class MultiSelectInputViewBackendBuilder<IVT, OT>(
    inputValue: Set<IVT>?,
    var mapping : SelectOptionMapping<IVT,OT>
) : AbstractSelectInputViewBackendBuilder<Set<IVT>, IVT, OT>(inputValue) {

    override fun toBackend() =
        MultiSelectInputViewBackend(inputValue, mapping, IdentityMultiInputMapping(), label, secret).also {
            setup(it)
        }

}