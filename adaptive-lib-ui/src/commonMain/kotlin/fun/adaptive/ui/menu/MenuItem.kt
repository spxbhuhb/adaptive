package `fun`.adaptive.ui.menu

class MenuItem(
    val title: String,
    val shortcut: String? = null,
    val onClick: () -> Unit
)