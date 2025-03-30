/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.ui.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.get
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.api.disabled
import `fun`.adaptive.ui.aui
import `fun`.adaptive.ui.instruction.input.InputPlaceholder
import `fun`.adaptive.ui.instruction.input.MaxLength
import `fun`.adaptive.ui.instruction.input.Secret
import `fun`.adaptive.ui.instruction.layout.Alignment
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual(aui)
open class AuiSingleLineTextInput(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int,
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, stateSize()) {

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    private var value: String?
        by stateVariable()

    private val validate: ((String?,String) -> Boolean)?
        by stateVariable()

    private val onChange: (String) -> Unit
        by stateVariable()

    override fun auiPatchInternal() {

        if (haveToPatchInstructions) {
            receiver.disabled = (disabled in instructions)

            val placeholder = get<InputPlaceholder>()?.value
            if (placeholder != null) {
                receiver.placeholder = placeholder
            }

            val maxLength = get<MaxLength>()?.maxLength
            if (maxLength != null) {
                receiver.maxLength = maxLength
            }

            val secret = get<Secret>()
            if (secret != null) {
                receiver.type = "password"
            }

            alignText()
        }

        if (haveToPatch(value)) {
            receiver.value = value ?: ""
        }

        if (isInit) {
            receiver.addEventListener("input", {
                if (receiver.value != value) {
                    if (validate?.invoke(value, receiver.value) == false) {
                        receiver.value = value ?: ""
                    } else {
                        value = receiver.value
                        onChange(receiver.value)
                    }
                }
            })
        }
    }

    fun alignText() {
        val alignment = renderData.container?.horizontalAlignment
        if (alignment != null) {
            receiver.style.textAlign = when (alignment) {
                Alignment.Start -> "start"
                Alignment.Center -> "center"
                Alignment.End -> "end"
            }
        } else {
            receiver.style.removeProperty("text-align")
        }
    }
}