package `fun`.adaptive.ui.tree

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import kotlin.properties.Delegates.observable

/**
 * Represents an item in a tree structure that can contain hierarchical data.
 *
 * @param T The type of data associated with this tree item
 * @property icon The graphical representation of this item
 * @property title The text displayed for this item
 * @property data The data associated with this item
 * @property open Whether this item is expanded to show its children
 * @property selected Whether this item is currently selected
 * @property parent The parent item of this tree item, null if this is a root item
 */
class TreeItem<T>(
    icon: GraphicsResourceSet?,
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
    var attachment by observable<Any?>(null, ::notify)

    override var value: TreeItem<T>
        get() = this
        set(_) {
            throw UnsupportedOperationException()
        }

    /**
     * Toggles the open state of this tree item.
     * If the item is open, it will be closed, and vice versa.
     */
    fun toggle() {
        open = ! open
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

    /**
     * Returns the previous visible item in the tree traversal order, respecting open
     * children and hierarchical navigation
     */
    fun previous(): TreeItem<T>? {
        val siblings = parent?.children ?: return null
        val index = siblings.indexOf(this)

        if (index == 0) {
            return parent
        }

        val previous = siblings[index - 1]

        fun lastVisibleChild(item: TreeItem<T>): TreeItem<T> {
            return if (item.open && item.children.isNotEmpty()) {
                lastVisibleChild(item.children.last())
            } else {
                item
            }
        }

        return lastVisibleChild(previous)
    }

    /**
     * Returns the next visible item in the tree traversal order, respecting open
     * children and hierarchical navigation
     */
    fun next(): TreeItem<T>? {
        if (open && children.isNotEmpty()) {
            return children.first()
        }

        var current: TreeItem<T>? = this
        while (current != null) {
            val siblings = current.parent?.children ?: return null
            val index = siblings.indexOf(current)
            if (index < siblings.size - 1) {
                return siblings[index + 1]
            }
            current = current.parent
        }

        return null
    }

    fun dumpTree(indent: Int = 0): String {
        val prefix = " ".repeat(indent * 2)
        val builder = StringBuilder()
        builder.append(prefix).append(if (open) "[-] " else "[+] ").append(title).append("\n")
        for (child in children) {
            builder.append(child.dumpTree(indent + 1))
        }
        return builder.toString()
    }
}