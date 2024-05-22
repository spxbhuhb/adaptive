/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.BoundingRect
import hu.simplexion.adaptive.utility.checkIfInstance

abstract class AdaptiveUIFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver : Any

    val frame
        get() = uiInstructions.frame

    // FIXME uiInstructions should be bound to instructions
    var uiInstructions = UIInstructions.DEFAULT

    fun fragmentFactory(index : Int) : BoundFragmentFactory =
        state[index].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun mount() {
        uiInstructions = UIInstructions(instructions)
        parent?.addActual(this, null) ?: adapter.addActual(this, null)
        super.mount()
    }

    override fun unmount() {
        super.unmount()
        parent?.removeActual(this) ?: adapter.removeActual(this)
    }

    open fun setFrame(frame : BoundingRect) {
        uiInstructions.frame = frame
    }

    /**
     * A pre-processed version of fragment instructions to make access from layout easier.
     */
    class UIInstructions(
        instructions : Array<out AdaptiveInstruction>
    ) {

        var frame : BoundingRect? = null
        val minSize : Float? = null
        val maxSize : Float? = null

        init {
            for (instruction in instructions) {
                when (instruction) {
                    is BoundingRect -> frame = instruction
                }
            }
        }

        companion object {
            val DEFAULT = UIInstructions(emptyArray())
        }
    }
}