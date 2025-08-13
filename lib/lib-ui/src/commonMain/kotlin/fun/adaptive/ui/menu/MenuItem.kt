package `fun`.adaptive.ui.menu

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.UUID
import kotlin.properties.Delegates.observable

@Adat
class MenuItem<T>(
    icon : GraphicsResourceSet?,
    label : String,
    data : T,
    shortcut : String? = null,
    inactive : Boolean = false,
    role : UUID<*>? = null,
    children : List<MenuItemBase<T>> = emptyList()
) : MenuItemBase<T>() {

    var icon by observable(icon, ::notify)
    var label by observable(label, ::notify)
    var shortcut by observable(shortcut, ::notify)
    var children by observable(children, ::notify)
    var data by observable(data, ::notify)
    var inactive by observable(inactive, ::notify)
    override var role by observable(role, ::notify)

    // FIXME Adat vs. observable
    // The @Adat annotation here does not do anything because all data is defined inside the class
    // as an observable.

    override fun adatEquals(other : Any?) : Boolean {
        return other is MenuItem<*> && super.adatEquals(other) &&
            icon == other.icon && label == other.label && shortcut == other.shortcut &&
            children == other.children && data == other.data && inactive == other.inactive && role == other.role
    }

}