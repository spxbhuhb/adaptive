package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.navigation.NavState

open class SidebarItem(
    val index: Int,
    val icon: DrawableResource,
    val title: String,
    val state: NavState
)