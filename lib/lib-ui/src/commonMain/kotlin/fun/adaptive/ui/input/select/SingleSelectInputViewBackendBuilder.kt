package `fun`.adaptive.ui.input.select

class SingleSelectInputViewBackendBuilder<VT>(
    inputValue: VT?
) : AbstractSelectInputViewBackendBuilder<VT, VT>(inputValue) {

    override fun toBackend() =
        SingleSelectInputViewBackend(inputValue, label, secret).also {
            setup(it as AbstractSelectInputViewBackend<VT, VT>)
        }

}