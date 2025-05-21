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
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.ui.instruction.layout.SizeBase
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
            val instructedFit = renderData.layout?.sizeStrategy

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

    override fun computeLayout(width: Double, height: Double) =
        super.computeLayout(SizingProposal(width, width, height, height))

    override fun computeLayout(
        proposal: SizingProposal
    ) {
        val proposedWidth = proposal.containerWidth
        val proposedHeight = proposal.containerHeight

        val innerWidth = renderData.innerWidth
        val innerHeight = renderData.innerHeight

        renderData.sizingProposal = proposal

        if (innerWidth != null && innerHeight != null) {
            computeLayout(innerWidth, innerHeight)
            return
        }

        val strategy = renderData.layout?.sizeStrategy

        if (strategy == null) {
            computeLayout(proposedWidth, proposedHeight)
            return
        }

        if (strategy.horizontalBase == SizeBase.Container) {
            computeLayout(proposedWidth, receiver.naturalHeight * proposedWidth / receiver.naturalWidth)
            return
        }

        if (strategy.verticalBase == SizeBase.Container) {
            computeLayout(proposedWidth, receiver.naturalHeight * proposedWidth / receiver.naturalWidth)
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