/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.ui.common.instruction.Font
import hu.simplexion.adaptive.ui.common.instruction.FontSize
import hu.simplexion.adaptive.utility.checkIfInstance

abstract class AdaptiveUIFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver : Any

    // FIXME uiInstructions should be bound to instructions
    var uiInstructions = UIInstructions.DEFAULT

    fun fragmentFactory(index : Int) : BoundFragmentFactory =
        state[index].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun mount() {
        uiInstructions = UIInstructions(instructions)
        parent?.addActual(this, null) ?: adapter.addActual(this)
        super.mount()
    }

    override fun unmount() {
        super.unmount()
        parent?.removeActual(this) ?: adapter.removeActual(this)
    }

    // ---------------------------------------------------------------------------------
    // Instruction support
    // ---------------------------------------------------------------------------------

    open var frame
        get() = uiInstructions.frame
        set(v) { uiInstructions.frame = v }

    /**
     * A pre-processed version of fragment instructions to make access from layout easier.
     */
    class UIInstructions(
        instructions : Array<out AdaptiveInstruction>
    ) {

        var frame : BoundingRect = BoundingRect.DEFAULT
        var minSize : Float? = null
        var maxSize : Float? = null
        var fontName: String? = null
        var fontSize: Float? = null

        init {
            for (instruction in instructions) {
                when (instruction) {
                    is BoundingRect -> frame = instruction
                    is Font -> fontName = instruction.fontName
                    is FontSize -> fontSize = instruction.fontSize
                }
            }
        }

        companion object {
            val DEFAULT = UIInstructions(emptyArray())
        }
    }
}