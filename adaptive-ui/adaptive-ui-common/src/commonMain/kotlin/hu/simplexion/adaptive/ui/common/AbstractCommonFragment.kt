/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.ui.common.support.RawFrame
import hu.simplexion.adaptive.ui.common.support.RawSize
import hu.simplexion.adaptive.ui.common.render.CommonRenderData

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

    // FIXME renderData should be bound to instructions
    var renderData = CommonRenderData.DEFAULT

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
     * The result of `measure` if the frame can calculate it. The basic fragments
     * such as images and text can calculate their own size which then can be
     * used for layout calculations or for resizing.
     */
    var measuredSize: RawSize = RawSize.NaS

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
            renderData = CommonRenderData(instructions)
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

    abstract fun measure(): RawSize

    protected fun instructedOr(measure: () -> RawSize): RawSize {
        val instructed = renderData.instructedSize

        if ( ! instructed.isNaS()) return instructed.toRaw(uiAdapter)

        val measured = measure()

        val size = RawSize(
            instructed.width.toPx(uiAdapter) or measured.width,
            instructed.height.toPx(uiAdapter) or measured.height
        )

        measuredSize = size
        traceMeasure()
        return size
    }

    fun traceMeasure() {
        if (trace) trace("measure", "measuredSize=${measuredSize}")
    }

    abstract fun layout(proposedFrame: RawFrame)

    protected infix fun Float.or(other : Float) =
        if (this.isNaN()) other else this

    open fun calcLayoutFrame(proposedFrame: RawFrame) {
        val instructedTop = renderData.instructedPoint.top.toPx(uiAdapter)
        val instructedLeft = renderData.instructedPoint.left.toPx(uiAdapter)

        val instructedWidth = renderData.instructedSize.width.toPx(uiAdapter)
        val instructedHeight = renderData.instructedSize.height.toPx(uiAdapter)

        layoutFrame = RawFrame(
            instructedTop or proposedFrame.point.top,
            instructedLeft or proposedFrame.point.left,
            instructedWidth or proposedFrame.size.width,
            instructedHeight or proposedFrame.size.height
        )

        traceLayout()
    }

    fun traceLayout() {
        if (trace) {
            trace(
                "layout",
                """
                    layoutFrame=${layoutFrame}
                    measuredSize=${measuredSize}
                    instructedPoint=${renderData.instructedPoint}
                    instructedSize=${renderData.instructedSize}
                """.trimIndent().replace("\n", " ")
            )
        }
    }

    fun toFrame(colOffsets: FloatArray, rowOffsets: FloatArray): RawFrame {
        val row = renderData.rowIndex
        val col = renderData.colIndex

        return RawFrame(
            rowOffsets[row],
            colOffsets[col],
            colOffsets[col + renderData.colSpan] - colOffsets[col],
            rowOffsets[row + renderData.rowSpan] - rowOffsets[row]
        )
    }

    val DPixel.px
        get() = uiAdapter.toPx(this)

}