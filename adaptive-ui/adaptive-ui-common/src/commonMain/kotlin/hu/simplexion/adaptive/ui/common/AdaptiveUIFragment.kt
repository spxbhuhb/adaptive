/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.layout.RawFrame
import hu.simplexion.adaptive.ui.common.layout.RawPoint
import hu.simplexion.adaptive.ui.common.layout.RawSize
import hu.simplexion.adaptive.utility.checkIfInstance

abstract class AdaptiveUIFragment<RT>(
    adapter: AdaptiveUIAdapter<*, RT>,
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
    var renderData = RenderData.DEFAULT

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
    var measuredSize: RawSize? = null

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
            renderData = RenderData(instructions)
            uiAdapter.applyRenderInstructions(this)
        }
    }

    override fun mount() {
        super.mount()
        parent?.addActual(this, null) ?: adapter.addActualRoot(this)
    }

    override fun unmount() {
        parent?.removeActual(this) ?: adapter.removeActualRoot(this)
        super.unmount()
    }

    abstract fun measure(): RawSize

    inline fun instructedOr(measured: () -> RawSize): RawSize {
        val size = renderData.instructedSize?.let { RawSize(it, uiAdapter) } ?: measured()
        measuredSize = size
        traceMeasure()
        return size
    }

    fun traceMeasure() {
        if (trace) trace("measure", "measuredSize=${measuredSize}")
    }

    abstract fun layout(proposedFrame: RawFrame)

    open fun calcLayoutFrame(proposedFrame: RawFrame) {
        val instructedPoint = renderData.instructedPoint?.let { RawPoint(it, uiAdapter) }
        val instructedSize = renderData.instructedSize?.let { RawSize(it, uiAdapter) }
        val measuredSize = this.measuredSize

        layoutFrame =

            if (instructedPoint != null) {
                if (instructedSize != null) {
                    RawFrame(instructedPoint, instructedSize)
                } else {
                    if (proposedFrame.size === RawSize.NaS && measuredSize != null) {
                        RawFrame(instructedPoint, measuredSize)
                    } else {
                        RawFrame(instructedPoint, proposedFrame.size)
                    }
                }
            } else {
                if (instructedSize != null) {
                    RawFrame(proposedFrame.point, instructedSize)
                } else {
                    if (proposedFrame.size === RawSize.NaS && measuredSize != null) {
                        RawFrame(proposedFrame.point, measuredSize)
                    } else {
                        proposedFrame
                    }
                }
            }

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

}