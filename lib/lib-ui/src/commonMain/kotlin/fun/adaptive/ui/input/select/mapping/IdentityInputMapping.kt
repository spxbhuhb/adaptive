package `fun`.adaptive.ui.input.select.mapping

import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

class IdentityInputMapping<INPUT_VALUE_TYPE> : SelectInputMapping<INPUT_VALUE_TYPE, INPUT_VALUE_TYPE> {

    override fun valueToItem(inputValue: INPUT_VALUE_TYPE?): INPUT_VALUE_TYPE? {
        return inputValue
    }

    override fun itemToValue(
        backend : AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, INPUT_VALUE_TYPE, *>,
        item: INPUT_VALUE_TYPE?
    ): INPUT_VALUE_TYPE? {
        return item
    }

}