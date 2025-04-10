package `fun`.adaptive.ui.filter

data class QuickFilterModel<T>(
    val selected: T,
    val entries: List<T>,
    val labelFun: (it: T) -> String,
    val theme: FilterTheme = FilterTheme.DEFAULT
)