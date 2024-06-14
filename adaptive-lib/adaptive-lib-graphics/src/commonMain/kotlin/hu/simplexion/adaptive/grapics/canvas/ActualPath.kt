/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.canvas

import hu.simplexion.adaptive.grapics.svg.instruction.Arc

interface ActualPath {
    fun moveTo(x: Float, y: Float)
    fun lineTo(x: Float, y: Float)
    fun closePath(x1: Float, y1: Float, x2: Float, y2: Float)
    fun arcTo(arc : Arc)
    fun cubicCurve(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float)
    fun smoothCubicCurve(x2: Float, y2: Float, x: Float, y: Float)
    fun quadraticCurve(x1: Float, y1: Float, x: Float, y: Float)
    fun smoothQuadraticCurve(x: Float, y: Float)
}