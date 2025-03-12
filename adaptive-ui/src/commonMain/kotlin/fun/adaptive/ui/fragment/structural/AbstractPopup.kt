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
import `fun`.adaptive.ui.instruction.layout.MaxHeight
import `fun`.adaptive.ui.instruction.layout.MaxWidth
import `fun`.adaptive.ui.instruction.layout.OuterAlignment
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.utility.alsoIfInstance

abstract class AbstractPopup<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int
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
            2 -> AdaptiveAnonymous(parent = parent, index = declarationIndex, stateSize = 2, factory = get(1))
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
                        value = BoundFragmentFactory(declaringFragment = this, declarationIndex = 2, null)
                    )
                }
            }

            2 -> {
                if (fragment.haveToPatch(closureDirtyMask, 1 shl 1)) {
                    fragment.setStateVariable(index = 1, value = ::hide)
                }
            }

            else -> invalidIndex(index)
        }
    }

    override fun patch() {
        super.patch()
        if (active) {
            placePopup()
        }
    }

    fun show() {
        if (active) return

        active = true
        patchInternal()

        placePopup()
    }

    fun hide() {
        active = false
        patchInternal()
    }

    override fun addActual(fragment: AdaptiveFragment, direct: Boolean?) {
        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->
            configureBox(itemFragment)
            uiAdapter.addActual(receiver, itemFragment.receiver)
            addActualScheduleUpdate(itemFragment)
        }
    }

    open fun configureBox(fragment: AbstractAuiFragment<RT>) {

    }

    override fun removeActual(fragment: AdaptiveFragment, direct: Boolean?) {
        fragment.alsoIfInstance<AbstractAuiFragment<RT>> { itemFragment ->
            uiAdapter.removeActual(itemFragment.receiver)
            removeActualScheduleUpdate(itemFragment)
        }
    }

    private fun placePopup() {

        val startPosition = this.absolutePosition

        val select = children.firstOrNull() ?: return
        val box = select.children.firstOrNull() ?: return
        if (box !is AbstractAuiFragment<*>) return

        val parentRenderData = renderData.layoutFragment?.renderData

        val maxWidth = instructions.firstInstanceOfOrNull<MaxWidth>()
        val maxHeight = instructions.lastInstanceOfOrNull<MaxHeight>()

        box.computeLayout(
            maxWidth?.let { parentRenderData?.finalWidth } ?: Double.POSITIVE_INFINITY,
            maxHeight?.let { parentRenderData?.finalHeight } ?: Double.POSITIVE_INFINITY,
        )

        var alignment = instructions.lastInstanceOfOrNull<PopupAlign>() ?: popupAlign.belowStart

        var position = RawPosition(0.0, 0.0)

        PopupAlign.findBestPopupAlignment(alignment) {
            position = getPosition(box.renderData, it)
            isPositionOk(startPosition, position, box.renderData)
        }

        box.placeLayout(position.top, position.left)
    }

    fun getPosition(popupRenderData: AuiRenderData, alignment: PopupAlign): RawPosition {
        val instructed = instructions.lastInstanceOfOrNull<Position>()
        if (instructed != null) return instructed.toRaw(uiAdapter)

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

    fun isPositionOk(startPosition: RawPosition, position: RawPosition, renderData: AuiRenderData): Boolean {
        val metrics = uiAdapter.mediaMetrics
        val width = metrics.viewWidth
        val height = metrics.viewHeight

        val x1 = startPosition.left + position.left
        val y1 = startPosition.top + position.top
        val x2 = x1 + renderData.finalWidth
        val y2 = y1 + renderData.finalHeight

        if (x1 < 0 || y1 < 0) return false
        if (x2 > width || y2 > height) return false

        return true
    }
}