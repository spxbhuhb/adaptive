package `fun`.adaptive.ui.menu

import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import kotlin.properties.Delegates.observable

open class MenuItem<T>(
    icon: GraphicsResourceSet?,
    label: String,
    data: T,
    shortcut: String? = null,
    inactive: Boolean = false,
    children: List<MenuItemBase<T>> = emptyList()
) : MenuItemBase<T>() {

    override var icon by observable(icon, ::notify)
    override var label by observable(label, ::notify)
    override var shortcut by observable(shortcut, ::notify)
    override var children by observable(children, ::notify)
    override var data by observable(data, ::notify)
    override var inactive by observable(inactive, ::notify)

}