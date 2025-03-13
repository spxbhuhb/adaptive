package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.unsupported
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

open class MenuSeparator<T> : MenuItemBase<T>() {

    override var icon: GraphicsResourceSet?
        get() = unsupported()
        set(_) {}

    override var label: String
        get() = unsupported()
        set(_) {}

    override var shortcut: String?
        get() = unsupported()
        set(_) {}

    override var children: List<MenuItemBase<T>>
        get() = unsupported()
        set(_) {}

    override var data: T
        get() = unsupported()
        set(_) {}

    override var inactive: Boolean
        get() = unsupported()
        set(_) {}

}