package `fun`.adaptive.ui.input.select.mapping

/**
 * Specifies how to convert [OPTION_TYPE] to [ITEM_TYPE].
 */
fun interface SelectOptionMapping<ITEM_TYPE, OPTION_TYPE> {
    fun optionToValue(option: OPTION_TYPE): ITEM_TYPE
}