/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.canvas

/**
 * Implemented by bridge classes to connect common code with the actual UI canvas implementation.
 */
interface ActualCanvas {

    /**
     * Called by [CanvasAdapter] to indicate that drawing on the canvas is about to begin.
     */
    fun startDraw()

    /**
     * Called by [CanvasAdapter] to indicate that drawing on the canvas has been ended.
     */
    fun endDraw()

    /**
     * Get an empty path that may be used for drawing, clipping etc.
     */
    fun newPath() : ActualPath

    /**
     * Fill a path
     */
    fun fill(path : ActualPath)
}