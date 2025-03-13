package `fun`.adaptive.ui.menu

import `fun`.adaptive.general.Observable
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

abstract class MenuItemBase<T> : Observable<MenuItemBase<T>>() {

    abstract var icon: GraphicsResourceSet?
    abstract var label: String
    abstract var shortcut: String?
    abstract var children: List<MenuItemBase<T>>
    abstract var data: T
    abstract var inactive: Boolean

    @Suppress("unused")
    fun <VT> notify(property: KProperty<*>, oldValue: VT, newValue: VT) {
        notifyListeners()
    }

    override var value: MenuItemBase<T>
        get() = this
        set(_) {
            throw UnsupportedOperationException()
        }

}