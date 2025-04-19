package `fun`.adaptive.ui.input.select

class MultiSelectInputViewBackendBuilder<OT>(
    inputValue: Set<OT>?
) : AbstractSelectInputViewBackendBuilder<Set<OT>, OT>(inputValue) {

    override fun toBackend() =
        MultiSelectInputViewBackend(inputValue, label, secret).also {
            setup(it)
        }

}