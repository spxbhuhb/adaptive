package `fun`.adaptive.ui.input.select.mapping

import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

/**
 * Multi select uses this mapping as a placeholder, it handles mapping by itself.
 */
class IdentityMultiInputMapping<INPUT_TYPE> : SelectInputMapping<Set<INPUT_TYPE>, INPUT_TYPE> {

    override fun valueToItem(inputValue: Set<INPUT_TYPE>?): INPUT_TYPE? {
        error("this code should never be executed")
    }

    override fun itemToValue(
        backend: AbstractSelectInputViewBackend<Set<INPUT_TYPE>, INPUT_TYPE, *>,
        item: INPUT_TYPE?
    ): Set<INPUT_TYPE>? {
        error("this code should never be executed")
    }

}