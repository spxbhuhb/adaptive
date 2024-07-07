/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.grapics.canvas.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveExpect
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.instruction.AdaptiveInstruction
import hu.simplexion.adaptive.foundation.manualImplementation
import hu.simplexion.adaptive.grapics.canvas.CanvasAdapter
import hu.simplexion.adaptive.grapics.canvas.CanvasFragment
import hu.simplexion.adaptive.grapics.canvas.canvas
import hu.simplexion.adaptive.grapics.svg.SvgAdapter
import hu.simplexion.adaptive.grapics.svg.parse.parseSvg
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.resource.defaultResourceReader
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AdaptiveExpect(canvas)
fun svg(resource : DrawableResource, vararg instructions: AdaptiveInstruction) {
    manualImplementation(resource, instructions)
}

@AdaptiveActual(canvas)
open class CanvasSvg(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int
) : CanvasFragment(adapter, parent, index, 1, 2) {

    val svgAdapter = SvgAdapter(adapter, canvas)

    var resource : DrawableResource
        get() = state[0].checkIfInstance()
        set(v) { setStateVariable(0,v) }

    override fun genPatchInternal(): Boolean {
        if (haveToPatch(dirtyMask, 1)) {
            // FIXME start resource read in a different thread and during mount maybe?
            CoroutineScope(adapter.dispatcher).launch {
                val data = defaultResourceReader.read(resource.path)
                svgAdapter.rootFragment = parseSvg(svgAdapter, data.decodeToString())
                svgAdapter.draw()
            }
        }

        return true
    }

    override fun draw() {

    }

}