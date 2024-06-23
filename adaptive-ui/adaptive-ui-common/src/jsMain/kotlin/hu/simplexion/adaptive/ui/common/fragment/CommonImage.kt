/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.ui.common.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.common
import hu.simplexion.adaptive.ui.common.support.layout.RawFrame
import hu.simplexion.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement

@AdaptiveActual(common)
open class CommonImage(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<HTMLElement>(adapter, parent, index, 1, 2) {

    override val receiver: HTMLImageElement =
        document.createElement("img") as HTMLImageElement

    private val res: DrawableResource
        get() = state[0].checkIfInstance()

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            receiver.src = res.uri
        }

        return false
    }

    override fun layout(proposedFrame: RawFrame?) {
        calcLayoutFrame(proposedFrame)

        receiver.width = layoutFrame.width.toInt()
        receiver.height = layoutFrame.height.toInt()

        uiAdapter.applyLayoutToActual(this)
    }

}