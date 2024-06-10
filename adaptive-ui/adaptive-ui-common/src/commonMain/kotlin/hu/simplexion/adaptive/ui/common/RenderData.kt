/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.testing.Traceable
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.layout.GridCell

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
 */
open class RenderData(
    instructions : Array<out AdaptiveInstruction>
) : Traceable, GridCell {

    override var tracePatterns : Array<out Regex> = emptyArray()

    var instructedPoint : Point = Point.NaP
    var instructedSize : Size = Size.NaS

    var color : Color? = null

    var padding : Padding = Padding.ZERO

    var fontName: String? = null
    var fontSize: SPixel? = null
    var fontWeight: Int? = null
    var letterSpacing: Float? = null
    var textAlign : TextAlign? = null
    var textWrap : TextWrap? = null

    var border: Border? = null
    var borderRadius: DPixel? = null

    var backgroundColor: Color? = null
    var backgroundGradient : BackgroundGradient? = null

    var alignContent : AlignContent? = null
    var alignItems : AlignItems? = null
    var alignSelf: AlignSelf? = null

    var justifyContent : JustifyContent? = null
    var justifyItems : JustifyItems? = null
    var justifySelf: JustifySelf? = null

    var gap : DPixel = DPixel.ZERO

    var noSelect : Boolean = false

    override var gridRow: Int? = null
    override var gridCol: Int? = null
    override var rowSpan: Int = 1
    override var colSpan: Int = 1
    override var rowIndex: Int = -1
    override var colIndex: Int = -1

    // TODO proper event handler management for UI fragments
    var onClick: OnClick? = null

    init {
        instructions.forEach{ it.apply(this) }
    }

    companion object {
        val DEFAULT = RenderData(emptyArray())
    }
}