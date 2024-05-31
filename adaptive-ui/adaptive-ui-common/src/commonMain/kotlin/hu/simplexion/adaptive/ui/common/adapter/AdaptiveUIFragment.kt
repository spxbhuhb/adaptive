/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.adapter

import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.foundation.internal.StateVariableMask
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.Size
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

    fun fragmentFactory(index: Int): BoundFragmentFactory =
        state[index].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? =
        null

    override fun genPatchDescendant(fragment: AdaptiveFragment) =
        Unit

    override fun genPatchInternal(): Boolean {
        val closureMask = getThisClosureDirtyMask()
        if (closureMask == 0) return false
        patchInstructions(closureMask)
        return true
    }

    fun patchInstructions(closureMask: StateVariableMask) {
        if (instructionIndex != - 1) {
            if (haveToPatch(closureMask, 1 shl instructionIndex)) {
                renderData = RenderData(instructions)
                uiAdapter.applyRenderInstructions(this)
            }
        }
    }

    override fun create() {
        super.create()
        parent?.addActual(this, null) ?: adapter.addActualRoot(this)
    }

    override fun dispose() {
        super.dispose()
        parent?.removeActual(this) ?: adapter.removeActualRoot(this)
    }

    abstract fun measure(): Size

    fun traceMeasure() {
        if (trace) trace("measure", "measuredSize=${renderData.measuredSize}")
    }

    open fun layout(proposedFrame: Frame) {
        uiAdapter.actualLayout(this, proposedFrame)
    }

    open fun setLayoutFrame(proposedFrame: Frame) {
        val instructedPoint = renderData.instructedPoint
        val instructedSize = renderData.instructedSize
        val measuredSize = renderData.measuredSize

        renderData.layoutFrame =

            if (instructedPoint != null) {
                if (instructedSize != null) {
                    Frame(instructedPoint, instructedSize)
                } else {
                    if (proposedFrame.size === Size.NaS && measuredSize != null) {
                        Frame(instructedPoint, measuredSize)
                    } else {
                        Frame(instructedPoint, proposedFrame.size)
                    }
                }
            } else {
                if (instructedSize != null) {
                    Frame(proposedFrame.point, instructedSize)
                } else {
                    if (proposedFrame.size === Size.NaS && measuredSize != null) {
                        Frame(proposedFrame.point, measuredSize)
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
                    layoutFrame=${renderData.layoutFrame}
                    measuredSize=${renderData.measuredSize}
                    instructedPoint=${renderData.instructedPoint}
                    instructedSize=${renderData.instructedSize}
                """.trimIndent().replace("\n", " ")
            )
        }
    }

    fun toFrame(colOffsets: FloatArray, rowOffsets: FloatArray): Frame {
        val row = renderData.rowIndex
        val col = renderData.colIndex

        return Frame(
            rowOffsets[row],
            colOffsets[col],
            colOffsets[col + renderData.colSpan] - colOffsets[col],
            rowOffsets[row + renderData.rowSpan] - rowOffsets[row]
        )
    }

}