/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.support.layout

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.fragment.AdaptiveAnonymous
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.utility.alsoIfInstance
import hu.simplexion.adaptive.utility.checkIfInstance

/**
 * Two uses: layouts and loop/select containers.
 *
 */
abstract class AbstractContainerFragment<RT, CRT : RT>(
    adapter: AbstractCommonAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AbstractCommonFragment<RT>(
    adapter, parent, declarationIndex, instructionsIndex, stateSize
) {

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
     * See [AbstractCommonAdapter.actualBatch].
     */
    override fun unmount() {
        if (uiAdapter.actualBatch) {
            super.unmount()
            return
        }

        try {
            uiAdapter.actualBatch = true
            super.unmount()
        } finally {
            uiAdapter.actualBatch = false

            // Unmount calls this, but it is useless as actualBatch is true at
            // that time, therefore it is a no-op. So, we have to call it manually.
            // FIXME manual remove actual after actual batch
            // I think this is somewhat incorrect because it may call adapter.removeActualRoot twice
            parent?.removeActual(this, if (isStructural) null else true)
        }
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("addActual", "fragment=$fragment, direct=$direct")

        fragment.alsoIfInstance<AbstractCommonFragment<RT>> { itemFragment ->

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
                itemFragment.measure()
                layout(layoutFrame)
            }
        }
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("removeActual", "fragment=$fragment")

        // when in a batch, everything will be removed at once
        if (uiAdapter.actualBatch) return

        fragment.alsoIfInstance<AbstractCommonFragment<RT>> { itemFragment ->

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

            if (isMounted) {
                layout(layoutFrame)
            }
        }
    }

}