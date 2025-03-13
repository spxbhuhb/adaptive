/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.canvas.instruction.CanvasTransformInstruction
import `fun`.adaptive.graphics.canvas.render.CanvasRenderData
import `fun`.adaptive.ui.fragment.layout.RawSize
import `fun`.adaptive.ui.fragment.layout.RawTextMeasurement
import `fun`.adaptive.ui.instruction.decoration.Color

/**
 * Implemented by bridge classes to connect common code with the actual UI canvas implementation.
 */
interface ActualCanvas {

    fun apply(renderData: CanvasRenderData?) {
        if (renderData == null) return

        renderData.fill?.let { setFill(it.color) }
        renderData.stroke?.let { setStroke(it.color) }
        renderData.transforms?.forEach { transform(it) }

        renderData.decoration?.backgroundColor?.let { setFill(it) }
        renderData.decoration?.border?.color?.let { setStroke(it) }
        renderData.text?.color?.let { setFill(it) }
    }

    /**
     * Draw on the canvas by calling [drawFun]. Implementations may surround [drawFun] with whatever
     * needed to notify the actual UI about the change.
     */
    fun draw(drawFun: () -> Unit)

    /**
     * Draw an arc.
     */
    fun arc(
        cx: Double,
        cy: Double,
        radius: Double,
        startAngle: Double,
        endAngle: Double,
        anticlockwise: Boolean
    )

    /**
     * Get an empty path that may be used for drawing, clipping etc.
     */
    fun newPath(): ActualPath

    /**
     * Save the current state of the canvas. Use [restore] to get back to the last save state.
     *
     * @param  id   Identifies the fragment that calls save. Subsequent calls of the same id are no-op.
     */
    fun save(id: Long)

    /**
     * Restore the last saved state of the canvas.
     *
     * @param  id   Identifies the fragment that calls restore. Restores only if the most recent save
     *              is from the same id.
     */
    fun restore(id: Long)

    /**
     * Fill a path
     */
    fun fill(path: ActualPath)

    /**
     * Fill a path
     */
    fun stroke(path: ActualPath)

    /**
     * Fill the current path.
     */
    fun fill()

    fun fillText(x: Double, y: Double, text: String)

    fun line(x1: Double, y1: Double, x2: Double, y2: Double)

    /**
     * Add a transform to the canvas.
     */
    fun transform(t: CanvasTransformInstruction)

    fun setFont(font: String)

    fun setStroke(color: Color)

    /**
     * Set the fill style for the canvas.
     */
    fun setFill(color: Color)

    /**
     * Clear the canvas.
     */
    fun clear()

    /**
     * Measure the dimensions of [text].
     */
    fun measureText(renderData : CanvasRenderData, text: String): RawTextMeasurement

}