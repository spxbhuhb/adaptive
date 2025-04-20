package `fun`.adaptive.ui.input.select.mapping

class IdentityMapping<T> : SelectOptionMapping<T, T> {
    override fun optionToValue(option: T): T = option
}