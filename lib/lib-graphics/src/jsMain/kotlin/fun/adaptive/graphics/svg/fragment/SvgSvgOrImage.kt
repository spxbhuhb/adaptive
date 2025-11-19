/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.graphics.svg.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.graphics.canvas.instruction.Fill
import `fun`.adaptive.graphics.svg.instruction.SvgHeight
import `fun`.adaptive.graphics.svg.instruction.SvgWidth
import `fun`.adaptive.graphics.svg.svg
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.resource.image.ImageResourceSet.Companion.remoteImage
import `fun`.adaptive.ui.AuiBrowserAdapter
import `fun`.adaptive.ui.fragment.AuiImage
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.layout.Height
import `fun`.adaptive.ui.instruction.layout.Size
import `fun`.adaptive.ui.instruction.layout.Width
import kotlinx.browser.window
import kotlin.math.roundToInt

@AdaptiveActual(svg)
class SvgSvgOrImage(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AdaptiveFragment(adapter, parent, index, stateSize()) {

    companion object {
        var imageMode = false
        //var iconServer = "https://icons.frel.io/v1/test-namespace"
        var iconServer = "http://127.0.0.1:8080/v1/test-namespace"
    }

    val resource: GraphicsResourceSet
        by stateVariable()

    override fun genBuild(parent: AdaptiveFragment, declarationIndex: Int, flags: Int): AdaptiveFragment {
        if (declarationIndex != 0) invalidIndex(declarationIndex)
        if (imageMode) {
            return AuiImage(adapter as AuiBrowserAdapter, this, declarationIndex).also { it.create() }
        } else {
            return SvgSvg(adapter as AuiBrowserAdapter, this, declarationIndex).also { it.create() }
        }
    }

    override fun genPatchInternal() : Boolean {
        return true
    }

    override fun genPatchDescendant(fragment: AdaptiveFragment) {
        if (imageMode) {
            (fragment as AuiImage).also {
                if (it.isInit || it.res != resource) {
                    it.res = toIconServerAsset()
                    it.dirtyMask = it.dirtyMask or 2
                }
                it.set(0, instructions)
                it.dirtyMask = it.dirtyMask or 1
            }
        } else {
            // FIXME hackish SGV patch
            (fragment as SvgSvg).also {
                if (it.isInit || it.resource != resource) {
                    it.resource = resource
                    it.dirtyMask = it.dirtyMask or 2
                }
                it.set(0, instructions)
                it.dirtyMask = it.dirtyMask or 1
            }
        }
    }

    private fun toIconServerAsset() : ImageResourceSet {
        // /v1/myapp/open_tree_node/1/24/24/1/ff0000ff/png

        val adapter = adapter as AuiBrowserAdapter

        var width = 24.0
        var height = 24.0
        var tint = Color(0x000000ffU)

        instructions.forEach {
            when (it) {
                is Size -> {
                    width = it.width.toRawValue(adapter)
                    height = it.height.toRawValue(adapter)
                }
                is Width -> width = it.width.toRawValue(adapter)
                is Height -> height = it.height.toRawValue(adapter)
                is SvgWidth -> width = it.width
                is SvgHeight -> height = it.height
                is Fill -> tint = it.color
            }
        }

        val dpr = run {
            try {
                val ratio = window.devicePixelRatio
                val i = ratio.roundToInt()
                (if (i < 1) 1 else i).toString()
            } catch (t: Throwable) {
                "1"
            }
        }

        val url = "${iconServer}/${resource.name}/1/${width}/${height}/${dpr}/${tint.hex.removePrefix("#")}/webp"
        return remoteImage(url)
    }
}