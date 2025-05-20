/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.fragment.layout.AbstractContainer
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.utility.alsoIfInstance

open class AuiStructural<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize: Int
) : AbstractContainer<RT, CRT>(adapter, parent, index, stateSize) {

    @Suppress("LeakingThis")
    override val receiver = adapter.makeStructuralReceiver(this)

    override val isStructural
        get() = true

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("addActual", "fragment=$fragment, structural=$this, direct=$direct")

        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->
            when (direct) {
                true -> {
                    directItems += itemFragment
                    uiAdapter.addActual(receiver, itemFragment.receiver)
                }

                false -> {
                    // do nothing, this does not concern us
                }

                null -> {
                    structuralItems += itemFragment
                    uiAdapter.addActual(receiver, itemFragment.receiver)
                }
            }

            parent?.addActual(fragment, if (direct == null) null else false)
        }
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("removeActual", "item=$fragment, structural=$this, direct=$direct")

        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->

            if (uiAdapter.actualBatchOwner != null && uiAdapter.actualBatchOwner != itemFragment.renderData.layoutFragment) return

            when (direct) {
                true -> {
                    directItems.removeAt(directItems.indexOfFirst { it.id == fragment.id })
                    uiAdapter.removeActual(itemFragment.receiver)
                }

                false -> {
                    // do nothing, this does not concern us
                }

                null -> {
                    structuralItems.removeAt(structuralItems.indexOfFirst { it.id == fragment.id })
                    uiAdapter.removeActual(itemFragment.receiver)
                }
            }

            parent?.removeActual(fragment, if (direct == null) null else false)
        }
    }

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        renderData.finalWidth = proposal.containerWidth
        renderData.finalHeight = proposal.containerHeight
    }

}
