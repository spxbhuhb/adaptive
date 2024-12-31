package `fun`.adaptive.ui.navigation.sidebar

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.navigation.NavState

open class SidebarItem(
    val icon: GraphicsResourceSet,
    val title: String,
    val state: NavState,
    val index: Int = indexCounter++
) {
    companion object {
        private var indexCounter = 0
    }
}