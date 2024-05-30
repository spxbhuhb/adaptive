/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.testing.Traceable
import hu.simplexion.adaptive.ui.common.logic.GridCell

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
 */
class RenderInstructions(
    instructions : Array<out AdaptiveInstruction>
) : Traceable, GridCell {

    override var tracePatterns : Array<out Regex> = emptyArray()

    /**
     * The actual frame of the fragment in the actual UI. Result of layout
     * calculations.
     */
    var layoutFrame : Frame = Frame.NaF

    /**
     * The result of `measure` if the frame can calculate it. The basic fragments
     * such as images and text can calculate their own size which then can be
     * used for layout calculations or for resizing.
     */
    var measuredSize : Size = Size.NaS

    /**
     * The frame specified by the instructions.
     */
    var instructedPoint : Point? = null

    var instructedSize : Size? = null

    var color : Color? = null

    var padding : Padding? = null

    var fontName: String? = null
    var fontSize: Float? = null
    var fontWeight: Int? = null
    var letterSpacing: Float? = null
    var textAlign : TextAlign? = null
    var textWrap : TextWrap? = null

    var border: Border? = null
    var borderRadius: Float? = null

    var backgroundColor: Color? = null
    var backgroundGradient : BackgroundGradient? = null

    var alignContent : AlignContent? = null
    var alignItems : AlignItems? = null
    var alignSelf: AlignSelf? = null

    var justifyContent : JustifyContent? = null
    var justifyItems : JustifyItems? = null
    var justifySelf: JustifySelf? = null

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
        val DEFAULT = RenderInstructions(emptyArray())
    }
}