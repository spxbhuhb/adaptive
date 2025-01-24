/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.utility.alsoIfInstance
import `fun`.adaptive.utility.checkIfInstance

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
    val content: BoundFragmentFactory?
        get() = get(state.size - 1)

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)

        // this supports empty containers for Grove (without a bound fragment factory, but with instructions)
        val safeContent = content
        if (safeContent == null) return null

        // FIXME I think this anonymous fragment is superfluous
        return AdaptiveAnonymous(this, declarationIndex, 1, safeContent).apply { create() }
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
                    layoutItems += itemFragment
                }

                null -> {
                    structuralItems += itemFragment
                    uiAdapter.addActual(receiver, itemFragment.receiver)
                }
            }

            scheduleUpdate()
        }
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

            scheduleUpdate()
        }
    }

    override fun auiPatchInternal() = Unit

    fun placeStructural() {
        for (item in structuralItems) {
            item.computeLayout(renderData.finalWidth, renderData.finalHeight)
            item.placeLayout(0.0, 0.0)
        }
    }

}