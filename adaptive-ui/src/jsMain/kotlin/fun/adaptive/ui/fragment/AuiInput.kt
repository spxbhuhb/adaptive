/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.utility.checkIfInstance
import `fun`.adaptive.utility.firstOrNullIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual(aui)
open class AuiInput(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 0, 2) {

    // 0 - instructions
    // 1 - binding

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    private val binding: AdaptiveStateVariableBinding<String>
        get() = state[1].checkIfInstance()

    override fun genPatchInternal(): Boolean {

        val b = binding

        patchInstructions()

        if (haveToPatch(dirtyMask, 1)) {
            receiver.value = b.value
        }

        if (isInit) {
            receiver.addEventListener("input", {
                if (receiver.value != b.value) {
                    b.setValue(receiver.value, true)
                }
            })

            receiver.style.outline = "none"

            val placeholder = instructions.firstOrNullIfInstance<InputPlaceholder>()?.value ?: b.path?.last()
            if (placeholder != null) {
                receiver.placeholder = placeholder
            }
        }

        return false
    }
}