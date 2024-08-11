/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grapics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveExpect
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.manualImplementation
import `fun`.adaptive.grapics.canvas.CanvasAdapter
import `fun`.adaptive.grapics.canvas.CanvasFragment
import `fun`.adaptive.grapics.canvas.canvas
import `fun`.adaptive.grapics.svg.SvgAdapter
import `fun`.adaptive.grapics.svg.parse.parseSvg
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.resource.defaultResourceReader
import `fun`.adaptive.utility.checkIfInstance
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