/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.instruction

import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.testing.Traceable

/**
 * A pre-processed version of fragment instructions to make access from layout easier.
 */
class RenderInstructions(
    instructions : Array<out AdaptiveInstruction>
) : Traceable {

    override var tracePatterns : Array<out Regex> = emptyArray()

    /**
     * This field is set by the layouts to place the fragment. When [frame] is
     * not null, it is typically the copy of it. Otherwise, it is the
     * responsibility of the layout to set it.
     */
    var layoutFrame : Frame = Frame.NaF

    /**
     * The frame specified in the code by the programmer.
     */
    var frame : Frame? = null
    var size : Size? = null

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

    var gridRow: Int? = null
    var gridCol: Int? = null
    var rowSpan: Int = 1
    var colSpan: Int = 1

    // TODO proper event handler management for UI fragments
    var onClick: OnClick? = null

    init {
        instructions.forEach{ it.apply(this) }
    }

    companion object {
        val DEFAULT = RenderInstructions(emptyArray())
    }
}