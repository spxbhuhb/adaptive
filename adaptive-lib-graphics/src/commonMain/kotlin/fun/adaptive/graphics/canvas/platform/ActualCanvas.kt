/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.graphics.canvas.platform

import `fun`.adaptive.graphics.svg.instruction.SvgFill
import `fun`.adaptive.graphics.svg.instruction.transform.SvgTransform

/**
 * Implemented by bridge classes to connect common code with the actual UI canvas implementation.
 *
 * @param  PT  Path type
 */
interface ActualCanvas {

    /**
     * Draw on the canvas by calling [drawFun]. Implementations may surround [drawFun] with whatever
     * needed to notify the actual UI about the change.
     */
    fun draw(drawFun : () -> Unit)

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
     * Add a transform to the canvas.
     */
    fun transform(t: SvgTransform)

    /**
     * Set the fill style for the canvas.
     */
    fun setFill(fill : SvgFill)

    /**
     * Clear the canvas.
     */
    fun clear()
}