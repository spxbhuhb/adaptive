/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.adapter

import hu.simplexion.adaptive.foundation.AdaptiveAdapter
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.internal.BoundFragmentFactory
import hu.simplexion.adaptive.ui.common.instruction.RenderInstructions
import hu.simplexion.adaptive.utility.checkIfInstance

abstract class AdaptiveUIFragment(
    adapter: AdaptiveAdapter,
    parent: AdaptiveFragment?,
    declarationIndex: Int,
    instructionsIndex: Int,
    stateSize : Int
) : AdaptiveFragment(adapter, parent, declarationIndex, instructionsIndex, stateSize) {

    abstract val receiver : Any

    // FIXME renderInstructions should be bound to instructions
    var renderInstructions = RenderInstructions.DEFAULT

    fun fragmentFactory(index : Int) : BoundFragmentFactory =
        state[index].checkIfInstance()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int): AdaptiveFragment? = null

    override fun genPatchDescendant(fragment: AdaptiveFragment) = Unit

    override fun genPatchInternal(): Boolean {
        if (instructionIndex != -1 && haveToPatch(getThisClosureDirtyMask(), instructionIndex)) {
            renderInstructions = RenderInstructions(instructions)
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

    open fun measure() {

    }

    open fun layout() {

    }

    // ---------------------------------------------------------------------------------
    // Instruction support
    // ---------------------------------------------------------------------------------

    open var frame
        get() = renderInstructions.frame
        set(v) { renderInstructions.frame = v }

}