/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement

@AdaptiveActual(aui)
open class AuiFlowText(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, stateSize()) {

    override val receiver: HTMLDivElement =
        document.createElement("div") as HTMLDivElement

    private val content: String
        by stateVariable()

    override fun auiPatchInternal() {

        if (! haveToPatch(content)) return

        val content = this.content

        if (receiver.textContent != content) {
            receiver.textContent = content
        }
    }

}