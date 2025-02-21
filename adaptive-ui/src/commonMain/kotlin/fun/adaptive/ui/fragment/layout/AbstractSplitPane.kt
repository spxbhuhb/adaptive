/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.throwAway
import `fun`.adaptive.foundation.throwChildrenAway
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility

/**
 * The split pane has two children configurations:
 *
 * Only one child is shown:
 * - box
 *
 * Both children are shown:
 * - box
 * - separator
 * - box
 */
abstract class AbstractSplitPane<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    declarationIndex: Int
) : AbstractContainer<RT, CRT>(
    adapter, parent, declarationIndex, 5
) {

    companion object {
        const val P1_BOX = 1
        const val P1_ANY = 2
        const val DI_BOX = 3
        const val DI_ANY = 4
        const val P2_BOX = 5
        const val P2_ANY = 6

        const val CONFIGURATION = 1
        const val PANE1_BUILDER = 2
        const val DIVIDER_BUILDER = 3
        const val PANE2_BUILDER = 4
    }

    val configuration get() = get<SplitPaneConfiguration>(CONFIGURATION)
    val pane1Builder get() = get<BoundFragmentFactory>(PANE1_BUILDER)
    val dividerBuilder get() = get<BoundFragmentFactory>(DIVIDER_BUILDER)
    val pane2Builder get() = get<BoundFragmentFactory>(PANE2_BUILDER)

    var currentVisibility: SplitVisibility? = null

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? {
        when (declarationIndex) {
            0 -> null
            P1_BOX -> adapter.actualize("aui:box", parent, P1_BOX, 2)
            P1_ANY -> AdaptiveAnonymous(parent, P1_ANY, 1, pane1Builder)
            DI_BOX -> adapter.actualize("aui:box", parent, DI_BOX, 2)
            DI_ANY -> AdaptiveAnonymous(parent, DI_ANY, 1, dividerBuilder)
            P2_BOX -> adapter.actualize("aui:box", parent, P2_BOX, 2)
            P2_ANY -> AdaptiveAnonymous(parent, P2_ANY, 1, pane2Builder)
            else -> invalidIndex(declarationIndex)
        }.also {
            it?.create()
            return it
        }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        val closureMask = fragment.getCreateClosureDirtyMask()
        when (fragment.declarationIndex) {
            P1_BOX -> if (haveToPatch(closureMask, 1 shl PANE1_BUILDER)) fragment.setStateVariable(1, BoundFragmentFactory(this, P1_ANY, null))
            P1_ANY -> if (haveToPatch(closureMask, 1 shl PANE1_BUILDER)) (fragment as AdaptiveAnonymous).factory = pane1Builder
            DI_BOX -> if (haveToPatch(closureMask, 1 shl DIVIDER_BUILDER)) fragment.setStateVariable(1, BoundFragmentFactory(this, DI_ANY, null))
            DI_ANY -> if (haveToPatch(closureMask, 1 shl DIVIDER_BUILDER)) (fragment as AdaptiveAnonymous).factory = dividerBuilder
            P2_BOX -> if (haveToPatch(closureMask, 1 shl PANE2_BUILDER)) fragment.setStateVariable(1, BoundFragmentFactory(this, P2_ANY, null))
            P2_ANY -> if (haveToPatch(closureMask, 1 shl PANE2_BUILDER)) (fragment as AdaptiveAnonymous).factory = pane2Builder
            else -> invalidIndex(fragment.declarationIndex)
        }
    }

    override fun auiPatchInternal() {
        if (! haveToPatch(dirtyMask, 1 shl CONFIGURATION)) return

        val c = configuration

        if (currentVisibility != c.visibility) {
            switchVisibility(c.visibility)
        }
    }

    /**
     * Switch between visibility modes. The switch can remove or add
     * children, depending on the difference between the current and
     * future visibilities.
     */
    fun switchVisibility(visibility: SplitVisibility) {

        when (currentVisibility) {
            SplitVisibility.Both -> {
                if (visibility == SplitVisibility.First) {
                    children.removeAt(2).throwAway() // throw away the second pane
                    children.removeAt(1).throwAway() // throw away the divider
                } else {
                    children.removeAt(0).throwAway() // throw away the first pane
                    children.removeAt(0).throwAway() // throw away the divider
                }
            }

            SplitVisibility.First -> {
                if (visibility == SplitVisibility.Both) {
                    children += genBuild(this, DI_BOX, 0) !!.also { it.mount() }
                    children += genBuild(this, P2_BOX, 0) !!.also { it.mount() }
                } else {
                    throwChildrenAway()
                    children += genBuild(this, P2_BOX, 0) !!.also { it.mount() }
                }
            }

            SplitVisibility.Second -> {
                if (visibility == SplitVisibility.Both) {
                    children.add(0, genBuild(this, P1_BOX, 0) !!.also { it.mount() })
                    children.add(1, genBuild(this, DI_BOX, 0) !!.also { it.mount() })
                } else {
                    throwChildrenAway()
                    children += genBuild(this, P1_BOX, 0) !!.also { it.mount() }
                }
            }

            null -> {
                if (visibility == SplitVisibility.Both || visibility == SplitVisibility.First) {
                    children += genBuild(this, P1_BOX, 0) !!.also { it.mount() }
                }
                if (visibility == SplitVisibility.Both) {
                    children.add(1, genBuild(this, DI_BOX, 0) !!.also { it.mount() })
                }
                if (visibility == SplitVisibility.Both || visibility == SplitVisibility.Second) {
                    children += genBuild(this, P2_BOX, 0) !!.also { it.mount() }
                }
            }
        }

        currentVisibility = visibility

        scheduleUpdate() // when visibility change we surely have to update the layout
    }

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        // Split pane always occupies the proposed space, there is no point in using a split pane
        // when the space is not fixed.
        // TODO do we have to force bounds in both directions in SplitPane?
        check(proposedWidth != unbound && proposedHeight != unbound)
        renderData.finalWidth = proposedWidth
        renderData.finalHeight = proposedHeight

        val c = configuration

        if (layoutItems.size == 1) {
            val pane = layoutItems.first()
            pane.computeLayout(proposedWidth, proposedHeight)
            pane.placeLayout(renderData.surroundingTop, renderData.surroundingStart)
            return
        }

        val pane1 = layoutItems[0]
        val divider = layoutItems[1]
        val pane2 = layoutItems[2]

        val dividerRawSize = c.dividerSize.toRawValue(uiAdapter)
        val horizontal = (c.orientation == Orientation.Horizontal)

        val availableWidth = if (horizontal) proposedWidth - dividerRawSize else proposedWidth
        val availableHeight = if (horizontal) proposedHeight else proposedHeight - dividerRawSize

        when (c.method) {
            SplitMethod.Proportional -> {
                if (horizontal) {
                    pane1.computeLayout(availableWidth * (1.0 - c.split), availableHeight)
                    pane2.computeLayout(availableWidth * c.split, availableHeight)
                } else {
                    pane1.computeLayout(availableWidth, availableHeight * (1.0 - c.split))
                    pane2.computeLayout(availableWidth, availableHeight * c.split)
                }
            }

            SplitMethod.FixFirst -> {
                if (horizontal) {
                    pane1.computeLayout(c.split, availableHeight)
                    pane2.computeLayout(availableWidth - c.split, availableHeight)
                } else {
                    pane1.computeLayout(availableWidth, c.split)
                    pane2.computeLayout(availableWidth, availableHeight - c.split)
                }
            }

            SplitMethod.FixSecond -> {
                if (horizontal) {
                    pane1.computeLayout(availableWidth - c.split, availableHeight)
                    pane2.computeLayout(c.split, availableHeight)
                } else {
                    pane1.computeLayout(availableWidth, availableHeight - c.split)
                    pane2.computeLayout(availableWidth, c.split)
                }
            }
        }

        if (horizontal) {
            divider.computeLayout(dividerRawSize, availableHeight)
        } else {
            divider.computeLayout(availableWidth, dividerRawSize)
        }

        if (horizontal) {
            pane1.placeLayout(renderData.surroundingTop, renderData.surroundingStart)
            divider.placeLayout(renderData.surroundingTop, pane1.renderData.finalLeft + pane1.renderData.finalWidth)
            pane2.placeLayout(renderData.surroundingTop, divider.renderData.finalLeft + divider.renderData.finalWidth)
        } else {
            pane1.placeLayout(renderData.surroundingTop, renderData.surroundingStart)
            divider.placeLayout(pane1.renderData.finalTop + pane1.renderData.finalHeight, renderData.surroundingStart)
            pane2.placeLayout(divider.renderData.finalTop + divider.renderData.finalHeight, renderData.surroundingStart)
        }
    }

//    override fun updateLayout(updateId: Long, item: AbstractAuiFragment<*>?) {
//        if (item == null) {
//            super.updateLayout(updateId, item)
//        } else {
//            updateItemLayout(updateId, item)
//        }
//    }
//
//    private fun updateItemLayout(updateId: Long, item: AbstractAuiFragment<*>) {
//        val data = this.renderData
//        val container = data.container
//
//        val innerWidth = data.innerWidth !!
//        val innerHeight = data.innerHeight !!
//
//        item.computeLayout(innerWidth, innerHeight)
//
//        item.placeLayout(innerTop + data.surroundingTop, innerLeft + data.surroundingStart)
//
//        item.updateBatchId = updateId
//    }

}