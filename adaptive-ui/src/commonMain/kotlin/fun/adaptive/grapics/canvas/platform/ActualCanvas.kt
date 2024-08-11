/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.grapics.canvas.platform

import `fun`.adaptive.grapics.svg.instruction.Fill
import `fun`.adaptive.grapics.svg.instruction.SvgTransform

/**
 * Implemented by bridge classes to connect common code with the actual UI canvas implementation.
 *
 * @param  PT  Path type
 */
interface ActualCanvas {

    /**
     * Get an empty path that may be used for drawing, clipping etc.
     */
    fun newPath(): ActualPath

    /**
     * Called by [CanvasAdapter] to indicate that drawing on the canvas is about to begin.
     */
    fun startDraw()

    /**
     * Called by [CanvasAdapter] to indicate that drawing on the canvas has been ended.
     */
    fun endDraw()

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
    fun setFill(fill : Fill)

}