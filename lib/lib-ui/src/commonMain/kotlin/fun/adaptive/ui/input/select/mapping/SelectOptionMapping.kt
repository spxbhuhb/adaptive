package `fun`.adaptive.ui.input.select.mapping

fun interface SelectOptionMapping<IVT, OVT> {
    fun optionToValue(option: OVT): IVT
}