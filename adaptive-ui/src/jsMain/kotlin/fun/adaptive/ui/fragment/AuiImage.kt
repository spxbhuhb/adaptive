/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

@AdaptiveActual(aui)
open class AuiImage(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    override val receiver: HTMLImageElement =
        document.createElement("img") as HTMLImageElement

    private val res: DrawableResource
        get() = state[0].checkIfInstance()

    override fun auiPatchInternal() {

        if (haveToPatch(dirtyMask, 1)) {
            receiver.src = res.uri
        }
    }

    override fun placeLayout(top: Double, left: Double) {
        receiver.width = renderData.finalWidth.toInt()
        receiver.height = renderData.finalHeight.toInt()
        super.placeLayout(top, left)
    }
}