package `fun`.adaptive.ui.input.select.mapping

class IdentityOptionMapping<T> : SelectOptionMapping<T, T> {
    override fun optionToValue(option: T): T = option
}