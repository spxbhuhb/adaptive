package `fun`.adaptive.ui.table

enum class Sorting {
    None,
    Descending,
    Ascending;

    val next : Sorting
        get() = when (this) {
            None -> Descending
            Descending -> Ascending
            Ascending -> None
        }
}