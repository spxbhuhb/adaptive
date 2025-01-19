/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual(aui)
open class AuiInput(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 0, stateSize()) {

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    val fakeInstructions: AdaptiveInstructionGroup
        by stateVariable()

    private var value: String?
        by stateVariable()

    private val onChange: (String) -> Unit
        by stateVariable()

    override fun auiPatchInternal() {

        if (haveToPatch(dirtyMask, 2)) {
            receiver.value = value ?: ""
        }

        if (isInit) {
            receiver.addEventListener("input", {
                if (receiver.value != value) {
                    value = receiver.value
                    onChange(receiver.value)
                }
            })
        }
    }
}