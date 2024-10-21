/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
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
open class AuiBoundInput(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 0, 4) {

    // 0 - instructions
    // 1 - binding
    // 2 - valueToString
    // 3 - valueFromString

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    private val binding: AdaptiveStateVariableBinding<Any>
        get() = state[1].checkIfInstance()

    private val valueToString: (Any) -> String
        get() = state[2].checkIfInstance()

    private val valueFromString: (String) -> Any?
        get() = state[3].checkIfInstance()

    private val invalid: (Boolean) -> Unit
        get() = state[4].checkIfInstance()

    override var invalidInput : Boolean = false

    override fun genPatchInternal(): Boolean {

        patchInstructions()

        // FIXME this is here because the value change does not change the binding itself
        // this results in a clear dirty mask and so the field is not updated
        val currentValue = valueToString(binding.value)

        if ((haveToPatch(dirtyMask, 2) || receiver.value != currentValue) && !invalidInput) {
            receiver.value = currentValue
        }

        if (isInit) {
            receiver.addEventListener("input", {

                if (receiver.value != binding.value) {
                    touch(binding)

                    try {
                        val newValue = valueFromString(receiver.value)
                        binding.setValue(newValue, true)
                        invalidInput = false

                    } catch (_: Throwable) {
                        // TODO think about value conversion exceptions in AuiBoundInput
                        // keep this silent, this is quite normal in most cases
                        invalidInput = true
                    }

                    invalid(invalidInput)
                }
            })

            // TODO should we merge AuiBoundInput and AuiInput?
            val propertyName = binding.path?.last()

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

    fun touch(binding: AdaptiveStateVariableBinding<Any>) {
        val instance = binding.stateVariableValue
        if (instance == null || instance !is AdatClass) return
        val context = instance.adatContext ?: return
        val path = binding.path ?: return
        context.touch(path)
    }
}