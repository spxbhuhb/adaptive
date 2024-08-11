/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.ui.common.AbstractCommonAdapter
import `fun`.adaptive.ui.common.AbstractCommonFragment
import `fun`.adaptive.utility.alsoIfInstance
import `fun`.adaptive.utility.checkIfInstance

/**
 * Two uses: layouts and loop/select containers.
 */
abstract class AbstractContainer<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AbstractCommonFragment<RT>(
    adapter, parent, declarationIndex, instructionsIndex, stateSize
) {

    val unbound
        get() = Double.POSITIVE_INFINITY

    @Suppress("LeakingThis") // instance construction should not perform any actions
    override val receiver: CRT = adapter.makeContainerReceiver(this)

    override val uiAdapter = adapter

    val layoutItems = mutableListOf<AbstractCommonFragment<RT>>() // Items to consider during layout.

    val directItems = mutableListOf<AbstractCommonFragment<RT>>() // Items to update directly, see class docs.

    val structuralItems = mutableListOf<AbstractCommonFragment<RT>>() // Items to update directly, see class docs.

    val content: BoundFragmentFactory
        get() = state[state.size - 1].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        // FIXME I think this anonymous fragment is superfluous
        return AdaptiveAnonymous(this, declarationIndex, 0, content).apply { create() }
    }

    /**
     * An optimization to remove the whole subtree of fragments from the actual UI at once.
     * See [AbstractCommonAdapter.actualBatchOwner].
     */
    override fun unmount() {
        if (uiAdapter.actualBatchOwner != null) {
            super.unmount()
            return
        }

        try {
            uiAdapter.actualBatchOwner = this.renderData.layoutFragment
            super.unmount()
        } finally {
            uiAdapter.actualBatchOwner = null
        }
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("addActual", "item=$fragment, direct=$direct")

        fragment.alsoIfInstance<AbstractCommonFragment<RT>> { itemFragment ->

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

            if (isMounted) {
                // FIXME layout with late additions may affect higher level layouts
                computeLayout(renderData.finalWidth, renderData.finalHeight)
            }
        }
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("removeActual", "item=$fragment, direct=$direct")

        fragment.alsoIfInstance<AbstractCommonFragment<RT>> { itemFragment ->

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

            if (isMounted) {// FIXME layout with late removals may affect higher level layouts
                computeLayout(renderData.finalWidth, renderData.finalHeight)
            }
        }
    }

    fun placeStructural() {
        for (item in structuralItems) {
            item.computeLayout(renderData.finalWidth, renderData.finalHeight)
            item.placeLayout(0.0, 0.0)
        }
    }

    abstract fun layoutChange(fragment: AbstractCommonFragment<*>)
}