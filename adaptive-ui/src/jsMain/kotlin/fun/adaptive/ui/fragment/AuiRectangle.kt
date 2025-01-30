/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.internal.cleanStateMask
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.render.model.AuiRenderData
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

@AdaptiveActual(aui)
open class AuiRectangle(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, stateSize()) {

    override val receiver: HTMLDivElement =
        document.createElement("div") as HTMLDivElement

    override val patchDescendants: Boolean
        get() = false

    override fun auiPatchInternal() = Unit

    override fun genPatchInternal(): Boolean {
        previousRenderData = renderData

        println("3.0")
        patchInstructions()
        println("3.1")

        auiPatchInternal()

        println("3.2")

        if (isInit || renderData.layoutIndependentChanged(previousRenderData)) {
            println("3.2.1")
            uiAdapter.applyLayoutIndependent(this)
        }

        println("3.3")


        if (renderData.innerDimensionsChanged(previousRenderData)) {
            scheduleUpdate()
            return patchDescendants
        }

        println("3.4")


        if (renderData.gridChanged(previousRenderData)) {
            // the optimization for grid and non-grid updates are different, hence the separation
            scheduleUpdate()
            return patchDescendants
        }

        println("3.5")


        if (renderData.layoutChanged(previousRenderData)) {
            scheduleUpdate()
            return patchDescendants
        }

        println("3.6")

        return patchDescendants
    }

    override fun patchInternal() {
        println("2.0")
        if (genPatchInternal()) {
            children.forEach { it.patch() }
        }
        println("2.1")

        dirtyMask = cleanStateMask

        println("2.2")
    }

    override fun create() {
        println("aaaaaaa")

        patchExternal()
        println("1")
        patchInternal()
        println("2")
        genBuild(this, 0, 0)?.let { children.add(it) }

        println("stuff")
    }
}