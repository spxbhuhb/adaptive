/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment.AdaptiveAnonymous
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.Trace
import `fun`.adaptive.foundation.internal.BoundFragmentFactory
import `fun`.adaptive.foundation.testing.AdaptiveTestAdapter
import `fun`.adaptive.utility.checkIfInstance
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlin.apply
import kotlin.let

@AdaptiveActual(canvas)
class TestCanvasCanvas(
    adapter: AdaptiveTestAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, 0, stateSize()) {

    val canvas = TestCanvas()

    val canvasAdapter = CanvasAdapter(adapter, canvas, this)

    val fakeInstructions: AdaptiveInstructionGroup
        by stateVariable()

    val content: BoundFragmentFactory
        by stateVariable()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        return AdaptiveAnonymous.switchAdapter(canvasAdapter, this, declarationIndex, 0, content).apply { create() }
    }

    override fun genPatchInternal(): Boolean {

        instructions.firstOrNullIfInstance<Trace>()?.let {
            canvasAdapter.trace = it.patterns
        }

        return true
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        // canvas does not patch descendants directly
    }

    override fun patchInternal() {
        super.patchInternal()
        canvasAdapter.draw()
    }

}