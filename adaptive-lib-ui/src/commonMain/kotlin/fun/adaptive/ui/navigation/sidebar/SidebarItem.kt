package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.navigation.NavState

open class SidebarItem(
    val icon: DrawableResource,
    val title: String,
    val state: NavState,
    val index: Int = indexCounter++
) {
    companion object {
        private var indexCounter = 0
    }
}