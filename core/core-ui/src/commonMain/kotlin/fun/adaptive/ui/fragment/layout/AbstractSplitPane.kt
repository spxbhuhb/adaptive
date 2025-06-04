/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.fragment.layout

import `fun`.adaptive.adat.api.update
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.throwAway
import `fun`.adaptive.foundation.throwChildrenAway
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.OnMove
import `fun`.adaptive.ui.instruction.layout.Orientation
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.SplitMethod
import `fun`.adaptive.ui.instruction.layout.SplitVisibility
import `fun`.adaptive.ui.instruction.toPx
import kotlin.math.max
import kotlin.math.min

/**
 * The split pane has three children configurations:
 *
 * No child is shown.
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
    adapter, parent, declarationIndex, 6
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
        const val ON_CHANGE = 5
    }

    val configuration get() = get<SplitPaneViewBackend>(CONFIGURATION)
    val pane1Builder get() = get<BoundFragmentFactory>(PANE1_BUILDER)
    val dividerBuilder get() = get<BoundFragmentFactory>(DIVIDER_BUILDER)
    val pane2Builder get() = get<BoundFragmentFactory>(PANE2_BUILDER)
    val onChange get() = get<((Double) -> Unit)?>(ON_CHANGE)

    var currentVisibility = SplitVisibility.None

    val wrap
        get() = configuration.method.let { it == SplitMethod.WrapFirst || it == SplitMethod.WrapSecond }

    // When moving, the primary down happens somewhere inside the divider.
    // We have to correct moves calculations with this amount to give
    // the expected user experience.

    var startPosition = Position.NaP
    var moveOffset: Double = 0.0 // device-dependent pixel

    // These must be over all the pane so we get the events even if the mouse
    // moves out of the divider a bit (which does happen).

    val splitPaneInstructions = instructionsOf(
        onMove { handleMove(it.x, it.y) },
        onLeave { handleMoveEnd() },
        onPrimaryUp { handleMoveEnd() }
    )

    // The content boxes must occupy the full size for the calculations to work.

    val paneInstructions = instructionsOf(
        //maxSize
    )

    // Only a primary down on the divider should start resize.

    val dividerInstructions = instructionsOf(
        onPrimaryDown {
            handleMoveStart(it.position, it.x, it.y)
            it.preventDefault() // to stop Safari from selecting text on move
        }
    )

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
            P1_BOX -> {
                if (haveToPatchConf(closureMask, 1)) {
                    fragment.setStateVariable(0, paneInstructions)
                }
                if (haveToPatchConf(closureMask, PANE1_BUILDER)) {
                    fragment.setStateVariable(1, BoundFragmentFactory(this, P1_ANY, null))
                }
            }

            P1_ANY -> {
                if (haveToPatchConf(closureMask, PANE1_BUILDER)) {
                    (fragment as AdaptiveAnonymous).factory = pane1Builder
                }
            }

            DI_BOX -> {
                if (! wrap && haveToPatchConf(closureMask, 1)) {
                    fragment.setStateVariable(0, dividerInstructions)
                }
                if (haveToPatchConf(closureMask, DIVIDER_BUILDER)) {
                    fragment.setStateVariable(1, BoundFragmentFactory(this, DI_ANY, null))
                }
            }

            DI_ANY -> {
                if (haveToPatchConf(closureMask, DIVIDER_BUILDER)) {
                    (fragment as AdaptiveAnonymous).factory = dividerBuilder
                }
            }

            P2_BOX -> {
                if (haveToPatchConf(closureMask, 1)) {
                    fragment.setStateVariable(0, paneInstructions)
                }
                if (haveToPatchConf(closureMask, PANE2_BUILDER)) {
                    fragment.setStateVariable(1, BoundFragmentFactory(this, P2_ANY, null))
                }
            }

            P2_ANY -> {
                if (haveToPatchConf(closureMask, PANE2_BUILDER)) {
                    (fragment as AdaptiveAnonymous).factory = pane2Builder
                }
            }

            else -> invalidIndex(fragment.declarationIndex)
        }
    }

    fun haveToPatchConf(closureMask: Int, d1: Int) =
        haveToPatch(closureMask, (1 shl d1) or (1 shl CONFIGURATION))

    override fun genPatchInternal(): Boolean {
        if (! wrap && instructions.firstInstanceOfOrNull<OnMove>() == null) {
            setStateVariable(0, instructions + splitPaneInstructions)
        }
        return super.genPatchInternal()
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
                when (visibility) {
                    SplitVisibility.First -> {
                        children.removeAt(2).throwAway() // throw away the second pane
                        children.removeAt(1).throwAway() // throw away the divider
                    }

                    SplitVisibility.Second -> {
                        children.removeAt(0).throwAway() // throw away the first pane
                        children.removeAt(0).throwAway() // throw away the divider
                    }

                    SplitVisibility.None -> {
                        throwChildrenAway()
                    }

                    else -> Unit
                }
            }

            SplitVisibility.First -> {
                when (visibility) {
                    SplitVisibility.Both -> {
                        children += genBuild(this, DI_BOX, 0) !!
                        children += genBuild(this, P2_BOX, 0) !!
                    }

                    SplitVisibility.Second -> {
                        throwChildrenAway()
                        children += genBuild(this, P2_BOX, 0) !!
                    }

                    SplitVisibility.First -> Unit

                    SplitVisibility.None -> {
                        throwChildrenAway()
                    }
                }
            }

            SplitVisibility.Second -> {
                when (visibility) {
                    SplitVisibility.Both -> {
                        children += genBuild(this, P1_BOX, 0) !!
                        children += genBuild(this, DI_BOX, 0) !!
                    }

                    SplitVisibility.First -> {
                        throwChildrenAway()
                        children += genBuild(this, P1_BOX, 0) !!
                    }

                    SplitVisibility.None -> {
                        throwChildrenAway()
                    }

                    SplitVisibility.Second -> Unit
                }
            }

            SplitVisibility.None -> {
                if (visibility == SplitVisibility.Both || visibility == SplitVisibility.First) {
                    children += genBuild(this, P1_BOX, 0) !!
                }
                if (visibility == SplitVisibility.Both) {
                    children += genBuild(this, DI_BOX, 0) !!
                }
                if (visibility == SplitVisibility.Both || visibility == SplitVisibility.Second) {
                    children += genBuild(this, P2_BOX, 0) !!
                }
            }
        }

        currentVisibility = visibility

        if (isMounted) {
            for (child in children) {
                if (! child.isMounted) child.mount()
            }
        }

        // the replacement logic above does not care about the order of the panes
        // these two calls fix it, so they will be shown in the proper order

        children.sortBy { it.declarationIndex }
        layoutItems.sortBy { it.declarationIndex }

        scheduleUpdate() // when visibility changes, we surely have to update the layout
    }

    override fun computeLayout(
        proposal : SizingProposal
    ) {
        traceLayoutCompute(proposal)

        // this is a general, bound case, updated when the method is WrapFirst or WrapSecond
        val proposedWidth = renderData.layout?.instructedWidth ?: proposal.maxWidth
        val proposedHeight = renderData.layout?.instructedHeight ?: proposal.maxHeight

        renderData.finalWidth = proposedWidth
        renderData.finalHeight = proposedHeight

        val top = renderData.surroundingTop
        val start = renderData.surroundingStart

        val fullAvailableWidth = proposedWidth - renderData.surroundingHorizontal
        val fullAvailableHeight = proposedHeight - renderData.surroundingVertical

        val c = configuration

        if (layoutItems.isEmpty()) return

        if (layoutItems.size == 1) {
            val pane = layoutItems.first()
            pane.computeLayout(fullAvailableWidth, fullAvailableHeight)
            pane.placeLayout(top, start)
            return
        }

        val pane1 = layoutItems[0]
        val divider = layoutItems[1]
        val pane2 = layoutItems[2]

        val dividerRawSize = c.dividerEffectiveSize.toRawValue(uiAdapter)
        val horizontal = (c.orientation == Orientation.Horizontal)

        var availableWidth = if (horizontal) fullAvailableWidth - dividerRawSize else fullAvailableWidth
        var availableHeight = if (horizontal) fullAvailableHeight else fullAvailableHeight - dividerRawSize

        val pane1Width: Double
        val pane1Height: Double

        val rawSplit = uiAdapter.toPx(c.split.dp)

        when (c.method) {
            SplitMethod.Proportional -> {
                if (horizontal) {
                    pane1Width = availableWidth * c.split
                    pane1Height = availableHeight
                } else {
                    pane1Width = availableWidth
                    pane1Height = availableHeight * c.split
                }
            }

            SplitMethod.FixFirst -> {
                if (horizontal) {
                    pane1Width = rawSplit
                    pane1Height = availableHeight
                } else {
                    pane1Width = availableWidth
                    pane1Height = rawSplit
                }
            }

            SplitMethod.FixSecond -> {
                if (horizontal) {
                    pane1Width = availableWidth - rawSplit
                    pane1Height = availableHeight
                } else {
                    pane1Width = availableWidth
                    pane1Height = availableHeight - rawSplit
                }
            }

            SplitMethod.WrapFirst -> {
                // calculate the size of the first pane with:
                // available size in main direction - split
                // proposed size in the other direction (might be unbound)
                if (horizontal) {
                    pane1.computeLayout(0.0, availableWidth - rawSplit, 0.0, availableHeight)
                } else {
                    pane1.computeLayout(0.0, availableWidth, 0.0, availableHeight - rawSplit)
                }

                pane1Width = pane1.renderData.finalWidth
                pane1Height = pane1.renderData.finalHeight

                if (horizontal) {
                    availableWidth = pane1Width + rawSplit
                    availableHeight = pane1Height
                    renderData.finalHeight = availableHeight + renderData.surroundingVertical
                } else {
                    availableWidth = pane1Width
                    availableHeight = pane1Height + rawSplit
                    renderData.finalWidth = availableWidth + renderData.surroundingHorizontal
                }
            }

            SplitMethod.WrapSecond -> {
                if (horizontal) {
                    pane2.computeLayout(0.0, availableWidth - rawSplit, 0.0, availableHeight)
                } else {
                    pane2.computeLayout(0.0, availableWidth, 0.0, availableHeight - rawSplit)
                }

                if (horizontal) {
                    availableHeight = pane2.renderData.finalHeight
                    renderData.finalHeight = availableHeight + renderData.surroundingVertical
                    pane1Width = availableWidth - pane2.renderData.finalWidth
                    pane1Height = availableHeight
                } else {
                    availableWidth = pane2.renderData.finalWidth
                    renderData.finalWidth = availableWidth + renderData.surroundingHorizontal
                    pane1Width = availableWidth
                    pane1Height = rawSplit
                }
            }

        }

        if (horizontal) {
            divider.computeLayout(dividerRawSize, fullAvailableHeight)
        } else {
            divider.computeLayout(fullAvailableWidth, dividerRawSize)
        }

        if (horizontal) {
            if (c.method != SplitMethod.WrapFirst) {
                pane1.computeLayout(pane1Width, availableHeight)
            }

            divider.computeLayout(c.dividerOverlaySize.pixelValue, availableHeight)

            if (c.method != SplitMethod.WrapSecond) {
                pane2.computeLayout(availableWidth - pane1Width, availableHeight)
            }
        } else {
            if (c.method != SplitMethod.WrapFirst) {
                pane1.computeLayout(availableWidth, pane1Height)
            }

            divider.computeLayout(availableWidth, c.dividerOverlaySize.pixelValue)

            if (c.method != SplitMethod.WrapSecond) {
                pane2.computeLayout(0.0, availableWidth, 0.0, availableHeight - pane1Height)
            }
        }

        val dividerOffset = (c.dividerOverlaySize - c.dividerEffectiveSize).toPx(uiAdapter) / 2

        val itemsTotalWidth: Double
        val itemsTotalHeight: Double

        if (horizontal) {
            val pane1FinalWidth = pane1.renderData.finalWidth

            pane1.placeLayout(top, start)
            divider.placeLayout(top, start + pane1FinalWidth - dividerOffset)
            pane2.placeLayout(top, start + dividerRawSize + pane1FinalWidth)

            itemsTotalWidth = pane1.renderData.finalWidth + dividerRawSize + pane2.renderData.finalWidth
            itemsTotalHeight = max(pane1.renderData.finalHeight, pane2.renderData.finalHeight)
        } else {
            val pane1FinalHeight = pane1.renderData.finalHeight

            pane1.placeLayout(top, start)
            divider.placeLayout(top + pane1FinalHeight - dividerOffset, start)
            pane2.placeLayout(top + pane1FinalHeight + dividerRawSize, start)

            itemsTotalWidth = max(pane1.renderData.finalWidth, pane2.renderData.finalWidth)
            itemsTotalHeight = pane1.renderData.finalHeight + dividerRawSize + pane2.renderData.finalHeight
        }


        computeFinal(proposal, itemsTotalWidth, itemsTotalHeight)
    }

    fun handleMoveStart(position: Position, x: Double, y: Double) {

        // position is inside the divider box.

        if (configuration.orientation == Orientation.Horizontal) {
            moveOffset = x
        } else {
            moveOffset = y
        }

        startPosition = position
    }

    fun handleMove(x: Double, y: Double) {
        if (startPosition === Position.NaP) return

        val c = configuration
        if (c.method == SplitMethod.WrapFirst || c.method == SplitMethod.WrapSecond) return

        // event position includes all surroundings: margin, border, padding
        // we have to ignore these in the calculations to set sizes properly
        // these effective values are cleared of surrounding data

        val effectiveX = x - renderData.surroundingStart
        val effectiveY = y - renderData.surroundingTop.dpValue
        val effectiveWidth = renderData.finalWidth - renderData.surroundingHorizontal
        val effectiveHeight = renderData.finalHeight - renderData.surroundingVertical

        val horizontal = (c.orientation == Orientation.Horizontal)

        val value: Double

        // the edges of the divider determine the sizes of the panes

        val dividerSize = uiAdapter.toPx(c.dividerEffectiveSize)
        val dividerStartEdge = (if (horizontal) effectiveX else effectiveY) - moveOffset
        val dividerEndEdge = dividerStartEdge + dividerSize

        when (c.method) {

            SplitMethod.FixFirst -> {
                value = dividerStartEdge
            }

            SplitMethod.FixSecond -> { // c.split is size of second in DP
                if (horizontal) {
                    value = effectiveWidth - dividerEndEdge
                } else {
                    value = effectiveHeight - dividerEndEdge
                }
            }

            SplitMethod.Proportional -> {
                if (horizontal) {
                    value = dividerStartEdge / (effectiveWidth - dividerSize)
                } else {
                    value = dividerStartEdge / (effectiveHeight - dividerSize)
                }
            }

            else -> return // this is handled above, but the compiler complains
        }

        val effectiveSize = (if (horizontal) effectiveWidth else effectiveHeight) - dividerSize

        val safeOnChange = onChange

        if (safeOnChange == null) {
            configuration.update(configuration::split, min(effectiveSize, max(0.0, value)).dpValue)
        } else {
            safeOnChange(value)
        }

        scheduleUpdate()
    }

    fun handleMoveEnd() {
        startPosition = Position.NaP
    }
}