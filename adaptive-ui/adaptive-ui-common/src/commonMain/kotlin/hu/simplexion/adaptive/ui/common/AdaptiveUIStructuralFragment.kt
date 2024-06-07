/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.alsoIfInstance

open class AdaptiveUIStructuralFragment<RT, CRT : RT>(
    adapter: AdaptiveUIAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize: Int
) : AdaptiveUIContainerFragment<RT, CRT>(adapter, parent, index, - 1, stateSize) {

    @Suppress("LeakingThis")
    override val receiver = adapter.makeStructuralReceiver(this)

    override val isStructural
        get() = true

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("addActual", "fragment=$fragment, structural=$this, direct=$direct")

        if (direct == true) {
            fragment.alsoIfInstance<AdaptiveUIFragment<RT>> { itemFragment ->
                directItems += itemFragment
                uiAdapter.addActual(receiver, itemFragment.receiver)
            }
        }

        parent!!.addActual(fragment, false)
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        if (trace) trace("removeActual", "fragment=$fragment, structural=$this, direct=$direct")

        // when in a batch, everything will be removed at once
        if (uiAdapter.actualBatch) return

        if (direct == true) {
            directItems.removeAt(directItems.indexOfFirst { it.id == fragment.id }).also {
                uiAdapter.removeActual(it.receiver)
            }
        }

        parent!!.removeActual(fragment, false)
    }

    override fun measure(): RawSize {
        return RawSize.NaS
    }

    override fun layout(proposedFrame: RawFrame) {
        // layout calculation is handled by the actual layouts, structural only applies to the actual UI
        layoutFrame = proposedFrame
        traceLayout()
    }
}
