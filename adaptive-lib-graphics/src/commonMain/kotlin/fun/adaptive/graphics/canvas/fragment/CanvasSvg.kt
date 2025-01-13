/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.canvas.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.CanvasAdapter
import `fun`.adaptive.graphics.canvas.CanvasFragment
import `fun`.adaptive.graphics.canvas.canvas
import `fun`.adaptive.graphics.svg.SvgAdapter
import `fun`.adaptive.graphics.svg.parse.SvgInstruction
import `fun`.adaptive.graphics.svg.parse.parseSvg
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.utility.checkIfInstance
import `fun`.adaptive.utility.safeCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AdaptiveActual(canvas)
open class CanvasSvg(
    adapter: CanvasAdapter,
    parent: AdaptiveFragment,
    index: Int
) : CanvasFragment(adapter, parent, index, 1, 2) {

    val svgAdapter = SvgAdapter(adapter, canvas).also { it.trace = adapter.trace }

    var resource: GraphicsResourceSet
        get() = state[0].checkIfInstance()
        set(v) {
            setStateVariable(0, v)
        }

    var data: ByteArray = byteArrayOf()

    override fun genPatchInternal(): Boolean {

        if (haveToPatch(dirtyMask, 1)) {
            CoroutineScope(Dispatchers.Default).launch {
                data = resource.readAll()
                CoroutineScope(adapter.dispatcher).launch {
                    parseAndDraw()
                }
            }
        }

        if (data.isNotEmpty() && haveToPatch(dirtyMask, 2)) {
            parseAndDraw()
        }

        return true
    }

    private fun parseAndDraw() {
        safeCall(svgLogger, message = "Couldn't parse resource: ${resource.uri}") {
            parseSvg(svgAdapter, data.decodeToString(), instructions.toMutableList().filterIsInstance<SvgInstruction>().toTypedArray())
        }?.let {
            svgAdapter.rootFragment = it
            svgAdapter.draw()
        }
    }

    override fun draw() {
        if (trace) trace("draw")
    }

    companion object {
        val svgLogger = getLogger("fun.adaptive.graphics.svg")
    }

}