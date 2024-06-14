/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.grapics.svg.instruction

import hu.simplexion.adaptive.grapics.svg.parse.SvgInstruction

class ViewBox(
    val minX : Float,
    val minY : Float,
    val width : Float,
    val height : Float
) : SvgInstruction

class Height(
    val height : String
) : SvgInstruction

class Width(
    val width : String
) : SvgInstruction

class Fill(
    val fill : String
) : SvgInstruction

class D(
    val commands : List<SvgPathCommand>
) : SvgInstruction