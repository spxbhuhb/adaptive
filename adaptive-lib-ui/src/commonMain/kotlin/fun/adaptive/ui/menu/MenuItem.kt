package `fun`.adaptive.ui.menu

import `fun`.adaptive.general.Observable
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.instruction.event.EventModifier
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

open class MenuItem<T>(
    icon: GraphicsResourceSet?,
    label: String,
    data: T,
    shortcut: String? = null,
    children: List<MenuItem<T>> = emptyList(),
    onClick: (MenuItem<T>, Set<EventModifier>) -> Unit = { _, _ -> }
) : Observable<MenuItem<T>>() {

    var icon by observable(icon, ::notify)
    var label by observable(label, ::notify)
    var shortcut by observable(shortcut, ::notify)
    var children by observable(children, ::notify)
    var data by observable(data, ::notify)
    var onClick by observable(onClick, ::notify)

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