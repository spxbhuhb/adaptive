/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.instruction.get
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.utility.checkIfInstance
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

        if (haveToPatch(dirtyMask, 2)) {
            receiver.value = b.value
        }

        if (isInit) {
            receiver.addEventListener("input", {
                if (receiver.value != b.value) {
                    b.setValue(receiver.value, true)
                }
            })

            receiver.style.outline = "none"

            val propertyName = b.path?.last()

            val placeholder = get<InputPlaceholder>()?.value ?: propertyName
            if (placeholder != null) {
                receiver.placeholder = placeholder
            }

            val metadata = binding.adatCompanion?.adatMetadata
            val descriptors = binding.adatCompanion?.adatDescriptors

            // TODO checking for the secret descriptor in input field is too expensive considering the number of uses in an application
            // however, if we will add more checks/customization properties it might be OK

            if (propertyName != null && metadata != null && descriptors != null && metadata[propertyName].isSecret(descriptors)) {
                receiver.type = "password"
            }
        }

        return false
    }
}