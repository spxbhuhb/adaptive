/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.fragment.layout.RawPosition
import `fun`.adaptive.ui.instruction.layout.*
import `fun`.adaptive.ui.render.model.AuiRenderData
import `fun`.adaptive.utility.alsoIfInstance

abstract class AbstractPopup<RT, CRT : RT>(
    adapter: AbstractAuiAdapter<RT, CRT>,
    parent: AdaptiveFragment?,
    index: Int,
    stateSize: Int
) : AuiStructural<RT, CRT>(
    adapter, parent, index, stateSize
) {

    companion object {
        const val SELECT_INDEX = 0
        const val ROOT_BOX_INDEX = 1
        const val CONTENT_INDEX = 2

        val ROOT_INSTRUCTIONS = instructionsOf(zIndex { 2000 })
    }

    var active = false

    override val patchDescendants: Boolean
        get() = true

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        when (declarationIndex) {
            SELECT_INDEX -> parent.adapter.newSelect(parent = parent, index = declarationIndex)
            ROOT_BOX_INDEX -> parent.adapter.actualize(name = "aui:rootbox", parent = parent, index = declarationIndex, stateSize = 2)
            CONTENT_INDEX -> AdaptiveAnonymous(parent = parent, index = declarationIndex, stateSize = 2, factory = content)
            else -> invalidIndex(index = declarationIndex)
        }.also {
            it.create()
        }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        val closureDirtyMask = fragment.getCreateClosureDirtyMask()
        val index = fragment.declarationIndex

        when (index) {

            SELECT_INDEX -> fragment.setStateVariable(1, if (active) 1 else - 1)

            ROOT_BOX_INDEX -> {
                if (fragment.haveToPatch(closureDirtyMask, 1)) {
                    fragment.setStateVariable(index = 0, value = instructions + ROOT_INSTRUCTIONS)
                }
                if (fragment.haveToPatch(closureDirtyMask, 0)) {
                    fragment.setStateVariable(
                        index = 1,
                        value = BoundFragmentFactory(declaringFragment = this, declarationIndex = CONTENT_INDEX, null)
                    )
                }
            }

            CONTENT_INDEX -> {
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

    open fun hide() {
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

        val layoutFragment = renderData.layoutFragment ?: return
        val startPosition = layoutFragment.absolutePosition

        val select = children.firstOrNull() ?: return
        val box = select.children.firstOrNull() ?: return
        if (box !is AbstractAuiFragment<*>) return

        val parentRenderData = layoutFragment.renderData

        val maxWidth = instructions.firstInstanceOfOrNull<MaxWidth>()
        val maxHeight = instructions.lastInstanceOfOrNull<MaxHeight>()

        box.computeLayout(
            maxWidth?.let { parentRenderData.finalWidth } ?: Double.POSITIVE_INFINITY,
            maxHeight?.let { parentRenderData.finalHeight } ?: Double.POSITIVE_INFINITY,
        )

        var alignment = instructions.lastInstanceOfOrNull<PopupAlign>() ?: popupAlign.belowStart

        var position = RawPosition(0.0, 0.0)

        PopupAlign.findBestPopupAlignment(alignment) {
            position = getPosition(box.renderData, it)
            isPositionOk(startPosition, position, box.renderData)
        }

        box.placeLayout(startPosition.top + position.top, startPosition.left + position.left)
    }

    fun getPosition(popupRenderData: AuiRenderData, alignment: PopupAlign): RawPosition {
        val instructed = instructions.lastInstanceOfOrNull<Position>()
        if (instructed != null) return instructed.toRaw(uiAdapter)

        val parentRenderData = renderData.layoutFragment?.renderData ?: return RawPosition(0.0, 0.0)
        val parentFrame = parentRenderData.rawFrame

        val left = when (alignment.horizontal) {
            OuterAlignment.Before -> - popupRenderData.finalWidth
            OuterAlignment.Start -> 0.0
            OuterAlignment.Center -> (parentFrame.width / 2) - (popupRenderData.finalWidth / 2)
            OuterAlignment.End -> parentFrame.width - popupRenderData.finalWidth
            OuterAlignment.After -> parentFrame.width
            else -> 0.0
        }

        val top = when (alignment.vertical) {
            OuterAlignment.Above -> - popupRenderData.finalHeight
            OuterAlignment.Start -> 0.0
            OuterAlignment.Center -> (parentFrame.height / 2) - (popupRenderData.finalHeight / 2)
            OuterAlignment.End -> parentFrame.height - popupRenderData.finalHeight
            OuterAlignment.Below -> parentFrame.height
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