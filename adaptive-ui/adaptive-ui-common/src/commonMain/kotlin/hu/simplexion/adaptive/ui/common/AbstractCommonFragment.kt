/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.render.CommonRenderData
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.ui.common.support.RawSurrounding

abstract class AbstractCommonFragment<RT>(
    adapter: AbstractCommonAdapter<RT, *>,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver: RT

    /**
     * Use this field when accessing actual UI specific adapter functions.
     */
    open val uiAdapter = adapter

    var renderData = CommonRenderData(adapter)

    /**
     * The actual frame of the fragment in the actual UI. Result of layout
     * calculations.
     */
    var layoutFrame: RawFrame
        get() = checkNotNull(layoutFrameOrNull) { "missing layout frame, probably an error in $this" }
        set(v) {
            layoutFrameOrNull = v
        }

    /**
     * The actual frame of the fragment in the actual UI. Result of layout
     * calculations.
     */
    var layoutFrameOrNull: RawFrame? = null

    /**
     * Structural fragments (loop and select) set this to true to modify behaviour.
     */
    open val isStructural
        get() = false

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        patchInstructions()
        return true
    }

    fun patchInstructions() {
        if (instructionIndex != - 1 && haveToPatch(dirtyMask, 1 shl instructionIndex)) {
            renderData = CommonRenderData(uiAdapter, instructions)
            uiAdapter.applyRenderInstructions(this)
        }
    }

    override fun mount() {
        super.mount()
        parent?.addActual(this, if (isStructural) null else true) ?: adapter.addActualRoot(this)
    }

    override fun unmount() {
        parent?.removeActual(this, if (isStructural) null else true) ?: adapter.removeActualRoot(this)
        super.unmount()
    }

    open fun measure() {
        val instructedWidth = renderData.layout?.width
        val instructedHeight = renderData.layout?.height

        renderData.boxWidth = instructedWidth ?: renderData.calcBoxWidth()
        renderData.boxHeight = instructedHeight ?: renderData.calcBoxHeight()

        if (trace) trace("measure", "measuredSize=(${renderData.boxWidth},${renderData.boxHeight})")
    }

    abstract fun layout(proposedFrame: RawFrame?)

    open fun calcLayoutFrame(proposedFrame: RawFrame?) {
        val instructedLayout = renderData.layout

        layoutFrame =
            if (instructedLayout == null) {
                proposedFrame ?: RawFrame.AUTO
            } else {
                RawFrame(
                    instructedLayout.top ?: proposedFrame?.top ?: Double.NaN,
                    instructedLayout.left ?: proposedFrame?.left ?: Double.NaN,
                    instructedLayout.width ?: proposedFrame?.width ?: Double.NaN,
                    instructedLayout.height ?: proposedFrame?.height ?: Double.NaN
                )
            }

        traceLayout()
    }

    fun traceLayout() {
        if (trace) {
            trace(
                "layout",
                """
                    layoutFrame=${layoutFrame}
                    measuredSize=(${renderData.measuredWidth},${renderData.measuredHeight})
                    instructedTop=${renderData.layout?.top}
                    instructedLeft=${renderData.layout?.left}
                    instructedWidth=${renderData.layout?.width}
                    instructedHeight=${renderData.layout?.height}
                """.trimIndent().replace("\n", " ")
            )
        }
    }

    val DPixel.px
        get() = uiAdapter.toPx(this)

    val DPixel?.pxOrZero
        get() = if (this == null) 0.0 else uiAdapter.toPx(this)

    fun CommonRenderData.calcBoxWidth(): Double {

        var width = measuredWidth

        val layout = layout ?: return width

        layout.padding?.let {
            width += it.left
            width += it.right
        }

        layout.border?.let {
            width += it.left
            width += it.right
        }

        layout.margin?.let {
            width += it.left
            width += it.right
        }

        return width
    }

    fun CommonRenderData.calcBoxHeight(): Double {
        var height = measuredHeight

        val layout = layout ?: return height

        layout.padding?.let {
            height += it.top
            height += it.bottom
        }

        layout.border?.let {
            height += it.top
            height += it.bottom
        }

        layout.margin?.let {
            height += it.top
            height += it.bottom
        }

        return height
    }

    fun surrounding(): RawSurrounding {
        val padding = renderData.layout?.padding ?: RawSurrounding.ZERO
        val border = renderData.layout?.border ?: RawSurrounding.ZERO
        val margin = renderData.layout?.margin ?: RawSurrounding.ZERO

        return RawSurrounding(
            padding.top + border.top + margin.top,
            padding.left + border.left + margin.left,
            padding.right + border.right + margin.right,
            padding.bottom + border.bottom + margin.bottom
        )
    }
}