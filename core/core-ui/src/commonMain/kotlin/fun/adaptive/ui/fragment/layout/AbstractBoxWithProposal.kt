/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.throwChildrenAway
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment

abstract class AbstractBoxWithProposal<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    stateSize: Int = 2
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, stateSize
) {

    var lastContent: BoundFragmentFactory? = null
    var contentProposal: SizingProposal? = null

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        return null
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        if (content.functionReference == null) {
            content.declaringFragment.genPatchDescendant(fragment)
        }
    }

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        computeFinal(proposal, proposal.containerWidth, proposal.containerHeight)

        addLayoutTask {
            if (! isMounted) return@addLayoutTask

            if (children.isNotEmpty() && lastContent !== content) {
                lastContent = content
                throwChildrenAway()
            }

            val child = children.firstOrNull()
            contentProposal = SizingProposal(0.0, renderData.innerWidth !!, 0.0, renderData.innerHeight !!)

            if (child == null) {
                // FIXME I think this anonymous fragment is superfluous
                AdaptiveAnonymous(this, declarationIndex, 2, content).also {
                    children += it
                    it.setStateVariable(1, contentProposal)
                    it.create()
                    it.mount() // adds layout updates
                }
                // TODO think about calling closePatchBach from boxWithProposal, I think it's fine
                adapter.closePatchBatch()
            } else {
                child.setStateVariable(1, contentProposal) // changes dirty mask if the proposal is different
                if (child.dirtyMask != 0) {
                    child.patchInternalBatch() // adds layout updates
                }
            }
        }
    }

    override fun updateLayout(updateId: Long, item: AbstractAuiFragment<*>?) {
        if (item == null) {
            super.updateLayout(updateId, item)
        } else {
            updateItemLayout(updateId, item)
        }
    }

    private fun updateItemLayout(updateId: Long, item: AbstractAuiFragment<*>) {
        val data = this.renderData
        val container = data.container

        val innerWidth = data.innerWidth ?: 0.0
        val innerHeight = data.innerHeight ?: 0.0

        item.computeLayout(innerWidth, innerHeight)

        val horizontalAlignment = container?.horizontalAlignment
        val verticalAlignment = container?.verticalAlignment

        val itemLayout = item.renderData.layout

        val innerTop = itemLayout?.instructedTop ?: alignOnAxis(item.verticalAlignment, verticalAlignment, innerHeight, item.renderData.finalHeight)
        val innerLeft = itemLayout?.instructedLeft ?: alignOnAxis(item.horizontalAlignment, horizontalAlignment, innerWidth, item.renderData.finalWidth)

        item.placeLayout(innerTop + data.surroundingTop, innerLeft + data.surroundingStart)

        item.updateBatchId = updateId
    }

}