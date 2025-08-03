/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.utility.alsoIfInstance

/**
 * Two uses: layouts and loop/select containers.
 *
 * State size is typically two, but in case of loop it is 3, therefore we cannot use `stateSize()`
 */
abstract class AbstractContainer<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize: Int
) : AbstractAuiFragment<RT>(
    adapter, parent, declarationIndex, stateSize
) {

    val unbound
        get() = Double.POSITIVE_INFINITY

    @Suppress("LeakingThis") // instance construction should not perform any actions
    override val receiver: CRT = adapter.makeContainerReceiver(this)

    override val uiAdapter = adapter

    override val patchDescendants: Boolean
        get() = true

    val layoutItems = mutableListOf<AbstractAuiFragment<RT>>() // Items to consider during layout.

    val directItems = mutableListOf<AbstractAuiFragment<RT>>() // Items to update directly, see class docs.

    val structuralItems = mutableListOf<AbstractAuiFragment<RT>>() // Items to update directly, see class docs.

    // State of containers has different sizes for different container types
    // We cannot use `by stateVariable()` here because of that.
    open val content: BoundFragmentFactory
        get() = get(state.size - 1)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)

        // FIXME I think this anonymous fragment is superfluous
        // if you change this you have to change AbstractBoxWithSizes as well
        return AdaptiveAnonymous(this, declarationIndex, 1, content).apply { create() }
    }

    /**
     * An optimization to remove the whole subtree of fragments from the actual UI at once.
     * See [AbstractAuiAdapter.actualBatchOwner].
     */
    override fun unmount() {
        if (uiAdapter.actualBatchOwner != null) {
            super.unmount()
            return
        }

        try {
            uiAdapter.actualBatchOwner = this.renderData.layoutFragment
            super.unmount() // calls removeActual
        } finally {
            uiAdapter.actualBatchOwner = null
        }
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("addActual", "item=$fragment, direct=$direct")

        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->

            itemFragment.renderData.layoutFragment = this

            when (direct) {
                true -> {
                    layoutItems += itemFragment
                    directItems += itemFragment
                    uiAdapter.addActual(receiver, itemFragment.receiver)
                }

                false -> {
                    addIndirect(itemFragment)
                }

                null -> {
                    structuralItems += itemFragment
                    uiAdapter.addActual(receiver, itemFragment.receiver)
                }
            }

            addActualScheduleUpdate(itemFragment)
        }
    }

    fun addIndirect(itemFragment: AbstractAuiFragment<RT>) {
        // We have to find out the previous layout sibling of the fragment, as we have
        // to insert after that item. The fragment may have any number of structural
        // and independent parents, so finding the previous layout sibling is not trivial.

        // If this container is initializing, the order is surely OK.
        // In this case we can skip finding the insert position.

        if (! hasBeenMounted) {
            layoutItems += itemFragment
            return
        }

        val layoutSibling = findClosestMatchingFragmentUpward(itemFragment) {
            it is AbstractAuiFragment<*> && ! it.isStructural
        }

        if (layoutSibling == null) {
            layoutItems.add(0, itemFragment)
        } else {
            @Suppress("UNCHECKED_CAST")
            addAfterSibling(itemFragment, layoutSibling as AbstractAuiFragment<RT>)
        }
    }

    fun findClosestMatchingFragmentUpward(
        start: AdaptiveFragment,
        predicate: (AdaptiveFragment) -> Boolean
    ): AdaptiveFragment? {
        var current: AdaptiveFragment? = start

        while (current !== this) {
            val parent = current?.parent ?: return null
            val index = parent.children.indexOf(current)

            // Check previous siblings and their descendants (depth-first)
            for (i in index - 1 downTo 0) {
                val match = findInSubtree(parent.children[i], predicate)
                if (match != null) return match
            }

            // Move up to the parent
            current = parent
        }

        return null
    }

    // Depth-first search within a subtree
    fun findInSubtree(node: AdaptiveFragment, predicate: (AdaptiveFragment) -> Boolean): AdaptiveFragment? {
        if (predicate(node)) return node
        val children = node.children
        for (index in children.lastIndex downTo 0) {
            val result = findInSubtree(children[index], predicate)
            if (result != null) return result
        }
        return null
    }

    fun addAfterSibling(itemFragment: AbstractAuiFragment<RT>, sibling: AbstractAuiFragment<RT>) {
        val index = layoutItems.indexOf(sibling)
        if (index == - 1) {
            layoutItems += itemFragment
        } else {
            layoutItems.add(index + 1, itemFragment)
        }
    }

    open fun addActualScheduleUpdate(itemFragment: AbstractAuiFragment<RT>) {
        scheduleUpdate()
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("removeActual", "item=$fragment, direct=$direct")

        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->

            // when in a batch, everything will be removed at once
            if (uiAdapter.actualBatchOwner != null && uiAdapter.actualBatchOwner != itemFragment.renderData.layoutFragment) return

            itemFragment.renderData.layoutFragment = null

            when (direct) {
                true -> {
                    layoutItems.removeAt(layoutItems.indexOfFirst { it.id == fragment.id })
                    directItems.removeAt(directItems.indexOfFirst { it.id == fragment.id })
                    uiAdapter.removeActual(itemFragment.receiver)
                }

                false -> {
                    layoutItems.removeAt(layoutItems.indexOfFirst { it.id == fragment.id })
                }

                null -> {
                    structuralItems.removeAt(structuralItems.indexOfFirst { it.id == fragment.id })
                    uiAdapter.removeActual(itemFragment.receiver)
                }
            }

            removeActualScheduleUpdate(itemFragment)
        }
    }

    open fun removeActualScheduleUpdate(itemFragment: AbstractAuiFragment<RT>) {
        scheduleUpdate()
    }

    override fun auiPatchInternal() = Unit

    fun placeStructural() {
        for (item in structuralItems) {
            item.computeLayout(renderData.finalWidth, renderData.finalHeight)
            item.placeLayout(0.0, 0.0)
        }
    }

}