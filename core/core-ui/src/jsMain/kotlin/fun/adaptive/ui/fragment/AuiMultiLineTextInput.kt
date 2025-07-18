/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiBrowserAdapter
import `fun`.adaptive.ui.api.disabled
import `fun`.adaptive.ui.aui
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTextAreaElement

@AdaptiveActual(aui)
open class AuiMultiLineTextInput(
    adapter: AuiBrowserAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, stateSize()) {

    override val receiver: HTMLTextAreaElement =
        document.createElement("textarea") as HTMLTextAreaElement

    private var value: String?
        by stateVariable()

    private val onChange: (String) -> Unit
        by stateVariable()

    override fun auiPatchInternal() {

        if (haveToPatchInstructions) {
            receiver.disabled = (disabled in instructions)
        }

        if (haveToPatch(value)) {
            receiver.value = value ?: ""
        }

        if (isInit) {
            receiver.addEventListener("input", {
                if (receiver.value != value) {
                    value = receiver.value
                    onChange(receiver.value)
                }
            })
            receiver.style.resize = "none"
        }
    }
}