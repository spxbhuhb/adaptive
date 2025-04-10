/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.instruction.layout.FitStrategy
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

@AdaptiveActual(aui)
open class AuiImage(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, stateSize()) {

    override val receiver: HTMLImageElement =
        document.createElement("img") as HTMLImageElement

    private val res: ImageResourceSet
        by stateVariable()

    private var loaded: Boolean
        by stateVariable()

    override fun create() {
        super.create()
        receiver.onload = {
            set(2, true) // set loaded to true
            setDirtyBatch(2)
        }
    }

    override fun auiPatchInternal() {
        if (haveToPatch(res)) {
            set(2, false) // set loaded to false, do not patch
            receiver.src = res.uri
        }

        // skip measurement when the size is instructed
        val layout = renderData.layout

        val instructedWidth = layout?.instructedWidth
        val instructedHeight = layout?.instructedHeight

        if (instructedWidth != null && instructedHeight != null) {
            renderData.innerWidth = instructedWidth
            renderData.innerHeight = instructedHeight
            return
        }

        if (loaded) {
            val instructedFit = renderData.layout?.fit

            val naturalWidth = receiver.naturalWidth.toDouble()
            val naturalHeight = receiver.naturalHeight.toDouble()

            val innerWidth : Double?
            val innerHeight : Double?

            when {
                instructedWidth != null -> {
                    innerWidth = instructedWidth
                    innerHeight = naturalHeight * instructedWidth / naturalWidth
                }
                instructedHeight != null -> {
                    innerWidth = naturalWidth * instructedHeight / naturalHeight
                    innerHeight = instructedHeight
                }
                instructedFit == null -> {
                    innerWidth = naturalWidth
                    innerHeight = naturalHeight
                }
                else -> {
                    innerWidth = null
                    innerHeight = null
                }
            }

            renderData.innerWidth = innerWidth
            renderData.innerHeight = innerHeight

            scheduleUpdate()
        }
    }

    override fun computeLayout(proposedWidth: Double, proposedHeight: Double) {
        val innerWidth = renderData.innerWidth
        val innerHeight = renderData.innerHeight

        if (innerWidth != null && innerHeight != null) {
            super.computeLayout(innerWidth, innerHeight)
            return
        }

        val instructedFit = renderData.layout?.fit

        if (instructedFit == null) {
            super.computeLayout(proposedWidth, proposedHeight)
            return
        }

        val horizontalStrategy = instructedFit.horizontalStrategy
        val verticalStrategy = instructedFit.verticalStrategy

        if (horizontalStrategy == FitStrategy.Container) {
            super.computeLayout(proposedWidth, receiver.naturalHeight * proposedWidth / receiver.naturalWidth)
            return
        }

        if (verticalStrategy == FitStrategy.Container) {
            super.computeLayout(proposedWidth, receiver.naturalHeight * proposedWidth / receiver.naturalWidth)
            return
        }

        error("Can't compute layout for image")
    }

    override fun placeLayout(top: Double, left: Double) {
        receiver.width = renderData.finalWidth.toInt()
        receiver.height = renderData.finalHeight.toInt()
        super.placeLayout(top, left)
    }
}