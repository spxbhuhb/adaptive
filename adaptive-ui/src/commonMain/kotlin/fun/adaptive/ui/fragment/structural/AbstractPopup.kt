/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.fragment.layout.RawSurrounding
import `fun`.adaptive.ui.instruction.layout.OuterAlignment
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.utility.alsoIfInstance

abstract class AbstractPopup<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
) : AuiStructural<RT, CRT>(
    adapter, parent, index, 2
) {

    var active = false

    override val patchDescendants: Boolean
        get() = true

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        when (declarationIndex) {
            0 -> parent.adapter.newSelect(parent = parent, index = declarationIndex)
            1 -> parent.adapter.actualize(name = "aui:box", parent = parent, index = declarationIndex, stateSize = 2)
            2 -> AdaptiveAnonymous(parent = parent, index = declarationIndex, stateSize = 1, factory = get(1))
            else -> invalidIndex(index = declarationIndex)
        }.also {
            it.create()
        }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        val closureDirtyMask = fragment.getCreateClosureDirtyMask()
        val index = fragment.declarationIndex

        when (index) {

            0 -> fragment.setStateVariable(1, if (active) 1 else - 1)

            1 -> {
                if (fragment.haveToPatch(closureDirtyMask, 1)) {
                    fragment.setStateVariable(index = 0, value = instructions)
                }
                if (fragment.haveToPatch(closureDirtyMask, 0)) {
                    fragment.setStateVariable(
                        index = 1,
                        value = BoundFragmentFactory(declaringFragment = this, declarationIndex = 2)
                    )
                }
            }

            2 -> Unit

            else -> invalidIndex(index)
        }
    }

    override fun patch() {
        super.patch()
        if (active) {
            val select = children.firstOrNull() ?: return
            val box = select.children.firstOrNull() ?: return
            if (box !is AbstractAuiFragment<*>) return

            box.computeLayout(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
            placePopup(box)
        }
    }

    fun show() {
        if (active) return

        active = true
        patchInternal()

        val select = children.firstOrNull() ?: return
        val box = select.children.firstOrNull() ?: return
        if (box !is AbstractAuiFragment<*>) return

        box.computeLayout(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
        placePopup(box)
    }

    fun hide() {
        active = false
        patchInternal()
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->
            uiAdapter.addActual(receiver, itemFragment.receiver)
            addActualScheduleUpdate(itemFragment)
        }
    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->
            uiAdapter.removeActual(itemFragment.receiver)
            removeActualScheduleUpdate(itemFragment)
        }
    }

    private fun placePopup(box: AbstractAuiFragment<*>) {
        val metrics = uiAdapter.mediaMetrics

        val position = getPosition(box.renderData)
        box.placeLayout(position.top, position.left)
    }

    fun getPosition(popupRenderData: AuiRenderData): RawPosition {
        val instructed = instructions.lastInstanceOfOrNull<Position>()
        if (instructed != null) return instructed.toRaw(uiAdapter)

        val alignment = instructions.lastInstanceOfOrNull<PopupAlign>() ?: popupAlign.belowStart
        val parentRenderData = renderData.layoutFragment?.renderData ?: return RawPosition(0.0, 0.0)
        val parentFrame = parentRenderData.rawFrame
        val parentBorder = parentRenderData.layout?.border ?: RawSurrounding.ZERO

        val left = when (alignment.horizontal) {
            OuterAlignment.Before -> - (popupRenderData.finalWidth + parentBorder.start)
            OuterAlignment.Start -> - parentBorder.start
            OuterAlignment.Center -> (parentFrame.width / 2) - (popupRenderData.finalWidth / 2) - parentBorder.start
            OuterAlignment.End -> parentFrame.width - popupRenderData.finalWidth - parentBorder.start
            OuterAlignment.After -> parentFrame.width - parentBorder.start
            else -> 0.0
        }

        val top = when (alignment.vertical) {
            OuterAlignment.Above -> - (popupRenderData.finalHeight + parentBorder.end)
            OuterAlignment.Start -> - parentBorder.top
            OuterAlignment.Center -> (parentFrame.height / 2) - (popupRenderData.finalHeight / 2) - parentBorder.top
            OuterAlignment.End -> parentFrame.height - popupRenderData.finalHeight - parentBorder.start
            OuterAlignment.Below -> parentFrame.height - parentBorder.start
            else -> 0.0
        }

        return RawPosition(top, left)
    }
}