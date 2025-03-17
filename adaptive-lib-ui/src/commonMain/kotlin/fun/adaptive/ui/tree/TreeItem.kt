package `fun`.adaptive.ui.tree

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import kotlin.properties.Delegates.observable

class TreeItem<T>(
    icon: GraphicsResourceSet,
    title: String,
    data: T,
    open: Boolean = false,
    selected: Boolean = false,
    val parent: TreeItem<T>?
) : SelfObservable<TreeItem<T>>() {

    var icon by observable(icon, ::notify)
    var title by observable(title, ::notify)
    var open by observable(open, ::notify)
    var selected by observable(selected, ::notify)
    var children by observable(emptyList<TreeItem<T>>(), ::notify)
    var data by observable(data, ::notify)

    override var value: TreeItem<T>
        get() = this
        set(_) {
            throw UnsupportedOperationException()
        }

    /**
     * Opens this tree node and all of its children recursively. This is a
     * **VERY** expensive operation. The innermost children are opened first.
     */
    fun expandAll() {
        children.forEach { it.expandAll() }
        open = true
    }

    /**
     * Closes this tree node and all of its children recursively. This is a
     * **VERY** expensive operation. Outermost nodes are closed first.
     */
    fun collapseAll() {
        open = false
        children.forEach { it.collapseAll() }
    }
}