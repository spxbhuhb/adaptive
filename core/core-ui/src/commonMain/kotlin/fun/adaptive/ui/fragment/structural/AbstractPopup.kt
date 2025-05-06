/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment.structural

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.query.first
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.layout.*
import `fun`.adaptive.ui.instruction.layout.*
import `fun`.adaptive.ui.render.model.AuiRenderData
import kotlin.math.max
import kotlin.math.min

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
        const val OVERLAY_INDEX = 1
        const val CONTAINER_INDEX = 2
        const val CONTENT_INDEX = 3

        val ROOT_INSTRUCTIONS = instructionsOf(maxSize, noPointerEvents, zIndex { 2000 })
        val ABSOLUTE_MODAL_ROOT_INSTRUCTIONS = instructionsOf(maxSize, zIndex { 2000 })
        val CONTAINER_INSTRUCTIONS = instructionsOf(enablePointerEvents, tabIndex { 0 })
    }

    var active = false

    override val patchDescendants: Boolean
        get() = true

    open val modal: Boolean
        get() = false

    open val positioningRenderData
        get() = renderData.layoutFragment?.renderData

    open val positioningFinalWidth: Double?
        get() = positioningRenderData?.finalWidth

    open val positioningFinalHeight: Double?
        get() = positioningRenderData?.finalHeight

    open val positioningAbsolutePosition: RawPosition?
        get() = renderData.layoutFragment?.absoluteViewportPosition

    open val positioningRawFrame: RawFrame?
        get() = positioningRenderData?.rawFrame

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment? =
        when (declarationIndex) {
            SELECT_INDEX -> parent.adapter.newSelect(parent = parent, index = declarationIndex)
            OVERLAY_INDEX -> buildOverlay(parent)
            CONTAINER_INDEX -> buildContainer(parent)
            CONTENT_INDEX -> AdaptiveAnonymous(parent = parent, index = declarationIndex, stateSize = 2, factory = content)
            else -> invalidIndex(index = declarationIndex)
        }.also {
            it.create()
        }

    private fun buildOverlay(parent: AdaptiveFragment): AdaptiveFragment {
        parent.adapter.actualize(name = "aui:manuallayout", parent = parent, index = OVERLAY_INDEX, stateSize = 2).also {
            it as AbstractManualLayout<*, *>
            it.isRootActual = true
            it.computeLayoutFun = ::computePopupLayout
            return it
        }
    }

    protected open fun buildContainer(parent: AdaptiveFragment): AbstractBox<RT, CRT> {
        parent.adapter.actualize(name = "aui:box", parent = parent, index = CONTAINER_INDEX, stateSize = 2).also {
            @Suppress("UNCHECKED_CAST")
            it as AbstractBox<RT, CRT>
            return it
        }
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        val closureDirtyMask = fragment.getCreateClosureDirtyMask()
        val index = fragment.declarationIndex

        when (index) {

            SELECT_INDEX -> fragment.setStateVariable(1, if (active) OVERLAY_INDEX else - 1)

            OVERLAY_INDEX -> {
                if (fragment.haveToPatch(closureDirtyMask, 1)) {
                    val absoluteModal = instructions.firstInstanceOfOrNull<PopupAlign>()?.modal != false
                    fragment.setStateVariable(index = 0, value = if (absoluteModal) ABSOLUTE_MODAL_ROOT_INSTRUCTIONS else ROOT_INSTRUCTIONS)
                }
                if (fragment.haveToPatch(closureDirtyMask, 0)) {
                    fragment.setStateVariable(
                        index = 1,
                        value = BoundFragmentFactory(declaringFragment = this, declarationIndex = CONTAINER_INDEX, null)
                    )
                }
            }

            CONTAINER_INDEX -> {
                if (fragment.haveToPatch(closureDirtyMask, 1)) {
                    fragment.setStateVariable(index = 0, value = instructions + CONTAINER_INSTRUCTIONS)
                    getOverlay()?.scheduleUpdate()
                }
                if (fragment.haveToPatch(closureDirtyMask, 0)) {
                    fragment.setStateVariable(
                        index = 1,
                        value = BoundFragmentFactory(declaringFragment = this, declarationIndex = CONTENT_INDEX, null)
                    )
                    getOverlay()?.scheduleUpdate()
                }
            }

            CONTENT_INDEX -> {
                if (closureDirtyMask != 0) {
                    patchContent(fragment)
                    getOverlay()?.scheduleUpdate()
                }
            }

            else -> invalidIndex(index)
        }
    }

    override fun unmount() {
        active = false
    }

    open fun patchContent(fragment: AdaptiveFragment) {
        fragment.setStateVariable(index = 1, value = ::hide)
    }

    open fun show() {
        if (active) return
        active = true
        patchInternal()
    }

    open fun hide() {
        if (! active) return
        active = false
        patchInternal()
    }

    fun getOverlay(): AbstractManualLayout<*, *>? {
        val select = children.first()
        return select.firstOrNull<AbstractManualLayout<*, *>>()
    }

    private fun computePopupLayout(proposedWidth: Double, proposedHeight: Double) {

        val layoutFinalWidth = positioningFinalWidth ?: return
        val layoutFinalHeight = positioningFinalHeight ?: return
        val layoutAbsolutePosition = positioningAbsolutePosition ?: return

        val overlay = getOverlay() ?: return
        overlay.computeFinal(proposedWidth, proposedWidth, proposedHeight, proposedWidth)

        val container = overlay.first<AbstractBox<*, *>>()


        val maxWidth = instructions.firstInstanceOfOrNull<MaxWidth>()
        val maxHeight = instructions.lastInstanceOfOrNull<MaxHeight>()

        container.computeLayout(
            maxWidth?.let { layoutFinalWidth } ?: Double.POSITIVE_INFINITY,
            maxHeight?.let { layoutFinalHeight } ?: Double.POSITIVE_INFINITY,
        )

        val alignment = instructions.lastInstanceOfOrNull<PopupAlign>() ?: popupAlign.belowStart

        if (alignment.absolute) {
            absolutePosition(alignment, overlay, container)
        } else {
            relativePosition(alignment, layoutAbsolutePosition, container)
        }
    }

    fun absolutePosition(
        alignment: PopupAlign,
        overlay: AbstractManualLayout<*, *>,
        container: AbstractBox<*, *>
    ) {
        val availableWidth = overlay.renderData.finalWidth
        val availableHeight = overlay.renderData.finalHeight

        val popupRenderData = container.renderData

        var top = (availableHeight - popupRenderData.finalHeight) / 2

        if (alignment.topMax != null) {
            top = min(top, uiAdapter.toPx(alignment.topMax))
        }

        container.placeLayout(
            max(0.0, top),
            max(0.0, (availableWidth - popupRenderData.finalWidth) / 2)
        )
    }

    fun relativePosition(
        alignment: PopupAlign,
        startPosition: RawPosition,
        container: AbstractBox<*, *>
    ) {
        var position = RawPosition(0.0, 0.0)

        PopupAlign.findBestPopupAlignment(alignment) {
            position = getPosition(container.renderData, it)
            isPositionOk(startPosition, position, container.renderData)
        }

        container.placeLayout(startPosition.top + position.top, startPosition.left + position.left)
    }

    fun getPosition(
        popupRenderData: AuiRenderData,
        alignment: PopupAlign
    ): RawPosition {
        val instructed = instructions.lastInstanceOfOrNull<Position>()
        if (instructed != null) return instructed.toRaw(uiAdapter)

        val parentFrame = positioningRawFrame ?: return RawPosition(0.0, 0.0)

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