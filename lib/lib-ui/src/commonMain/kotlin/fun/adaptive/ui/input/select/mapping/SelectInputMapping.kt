package `fun`.adaptive.ui.input.select.mapping

import `fun`.adaptive.ui.input.select.AbstractSelectInputViewBackend

/**
 * Specifies how to convert between [INPUT_VALUE_TYPE] and [ITEM_TYPE].
 */
interface SelectInputMapping<INPUT_VALUE_TYPE, ITEM_TYPE> {

    fun valueToItem(inputValue: INPUT_VALUE_TYPE?): ITEM_TYPE?

    fun itemToValue(
        backend : AbstractSelectInputViewBackend<INPUT_VALUE_TYPE, ITEM_TYPE, *>,
        item: ITEM_TYPE?
    ): INPUT_VALUE_TYPE?

}