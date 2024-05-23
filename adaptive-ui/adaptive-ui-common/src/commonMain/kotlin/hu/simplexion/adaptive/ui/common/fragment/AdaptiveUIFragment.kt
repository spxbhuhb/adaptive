/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.*
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

    override fun genPatchInternal(): Boolean {
        if (instructionIndex != -1 && haveToPatch(getThisClosureDirtyMask(), instructionIndex)) {
            uiInstructions = UIInstructions(instructions)
        }
        return true
    }

    override fun mount() {
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
        var color : Color? = null
        var minSize : Float? = null
        var maxSize : Float? = null
        var fontName: String? = null
        var fontSize: Float? = null
        var borderRadius: Float? = null
        var backgroundGradient : BackgroundGradient? = null

        init {
            instructions.forEach{
                if (it is AdaptiveUIInstruction) it.apply(this)
            }
        }

        companion object {
            val DEFAULT = UIInstructions(emptyArray())
        }
    }
}