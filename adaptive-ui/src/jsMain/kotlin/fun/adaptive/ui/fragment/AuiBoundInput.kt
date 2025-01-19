/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.get
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.ui.instruction.input.MaxLength
import `fun`.adaptive.utility.checkIfInstance
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual(aui)
open class AuiBoundInput(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 0, stateSize()) {

    // 0 - instructions
    // 1 - binding
    // 2 - valueToString
    // 3 - valueFromString
    // 4 - validityFun

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    val fakeInstructions: AdaptiveInstructionGroup
        by stateVariable()

    private val binding: AdaptiveStateVariableBinding<Any>
        by stateVariable()

    private val valueToString: (Any) -> String
        by stateVariable()

    private val valueFromString: (String) -> Any?
        by stateVariable()

    private val validityFun: (Boolean) -> Unit
        by stateVariable()

    override var invalidInput: Boolean = false

    override fun auiPatchInternal() {

        // FIXME this is here because the value change does not change the binding itself
        // this results in a clear dirty mask and so the field is not updated
        val currentValue = valueToString(binding.value)

        if ((haveToPatch(binding) || receiver.value != currentValue) && ! invalidInput) {
            receiver.value = currentValue
        }

        if (isInit) {
            init()
        }
    }

    fun init() {
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

                validityFun(! invalidInput)
            }
        })

        val propertyName = binding.path?.last()

        val placeholder = get<InputPlaceholder>()?.value ?: propertyName
        if (placeholder != null) {
            receiver.placeholder = placeholder
        }

        val maxLength = get<MaxLength>()?.maxLength
        if (maxLength != null) {
            receiver.maxLength = maxLength
        }

        val metadata = binding.adatCompanion?.adatMetadata
        val descriptors = binding.adatCompanion?.adatDescriptors

        // TODO checking for the secret descriptor in input field is too expensive considering the number of uses in an application
        // however, if we will add more checks/customization properties it might be OK

        if (propertyName != null && metadata != null && descriptors != null) {
            val property = metadata[propertyName]
            if (property.isSecret(descriptors)) receiver.type = "password"
        }
    }

    fun touch(binding: AdaptiveStateVariableBinding<Any>) {
        val instance = binding.stateVariableValue
        if (instance == null || instance !is AdatClass) return
        val context = instance.adatContext ?: return
        val path = binding.path ?: return
        context.touch(path)
    }
}