/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.Frame
import hu.simplexion.adaptive.ui.common.instruction.RenderInstructions
import hu.simplexion.adaptive.ui.common.instruction.Size
import hu.simplexion.adaptive.utility.checkIfInstance

abstract class AdaptiveUIFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize: Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver: Any

    // FIXME renderInstructions should be bound to instructions
    var renderInstructions = RenderInstructions.DEFAULT

    fun fragmentFactory(index: Int): BoundFragmentFactory =
        state[index].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean {
        if (instructionIndex != - 1) {
            if (haveToPatch(getThisClosureDirtyMask(), 1 shl instructionIndex)) {
                renderInstructions = RenderInstructions(instructions)
            }
        }
        return true
    }

    override fun create() {
        super.create()
        parent?.addActual(this, null) ?: adapter.addActual(this)
    }

    override fun mount() {
        measure()
        super.mount()
        layout(Frame.NaF)
    }

    override fun dispose() {
        super.dispose()
        parent?.removeActual(this) ?: adapter.removeActual(this)
    }

    abstract fun measure()

    fun traceMeasure() {
        if (trace) trace("measure", "measuredSize=${renderInstructions.measuredSize}")
    }

    open fun layout(proposedFrame: Frame) {
        val instructedPoint = renderInstructions.instructedPoint
        val instructedSize = renderInstructions.instructedSize

        renderInstructions.layoutFrame =

            if (instructedPoint != null) {
                if (instructedSize != null) {
                    Frame(instructedPoint, instructedSize)
                } else {
                    Frame(instructedPoint, proposedFrame.size)
                }
            } else {
                if (instructedSize != null) {
                    Frame(proposedFrame.point, instructedSize)
                } else {
                    proposedFrame
                }
            }

        traceLayout()
    }

    fun traceLayout() {
        if (trace) {
            trace(
                "layout",
                """
                    layoutFrame=${renderInstructions.layoutFrame}
                    measuredSize=${renderInstructions.measuredSize}
                    instructedPoint=${renderInstructions.instructedPoint}
                    instructedSize=${renderInstructions.instructedPoint}
                """.trimIndent().replace("\n", " ")
            )
        }
    }

    fun toFrame(colOffsets: FloatArray, rowOffsets: FloatArray): Frame {
        val row = renderInstructions.rowIndex
        val col = renderInstructions.rowIndex

        return Frame(
            rowOffsets[row],
            colOffsets[col],
            colOffsets[col + renderInstructions.colSpan] - colOffsets[col],
            rowOffsets[row + renderInstructions.rowSpan] - rowOffsets[row]
        )
    }

}