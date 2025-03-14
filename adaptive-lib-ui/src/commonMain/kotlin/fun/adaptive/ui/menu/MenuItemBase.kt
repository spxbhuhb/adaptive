package `fun`.adaptive.ui.menu

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.GraphicsResourceSet

abstract class MenuItemBase<T> : SelfObservable<MenuItemBase<T>>() {

    abstract var icon: GraphicsResourceSet?
    abstract var label: String
    abstract var shortcut: String?
    abstract var children: List<MenuItemBase<T>>
    abstract var data: T
    abstract var inactive: Boolean

}