package `fun`.adaptive.ui.menu

import `fun`.adaptive.general.Observable
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

open class MenuItem<T>(
    icon: GraphicsResourceSet?,
    label: String,
    data: T,
    shortcut: String? = null,
    children: List<MenuItem<T>> = emptyList()
) : Observable<MenuItem<T>>() {

    var icon by observable(icon, ::notify)
    var label by observable(label, ::notify)
    var shortcut by observable(shortcut, ::notify)
    var children by observable(children, ::notify)
    var data by observable(data, ::notify)

    @Suppress("unused")
    fun <VT> notify(property : KProperty<*>, oldValue : VT, newValue : VT) {
        notifyListeners()
    }

    override var value: MenuItem<T>
        get() = this
        set(_) {
            throw UnsupportedOperationException()
        }

}