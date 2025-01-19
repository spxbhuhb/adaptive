/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove.fragment

import `fun`.adaptive.foundation.AdaptiveActual
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.ui.AbstractAuiFragment
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.utility.checkIfInstance
import `fun`.adaptive.utility.format
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual
open class GroveDPixelInput(
    adapter: AuiAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractAuiFragment<HTMLElement>(adapter, parent, index, 0, stateSize()) {

    // 0 - instructions
    // 1 - binding

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    private val binding: AdaptiveStateVariableBinding<DPixel?>
        by stateVariable()

    override fun auiPatchInternal() {

        val b = binding

        if (haveToPatch(dirtyMask, 1 shl 1)) {
            val value = b.value
            if (value == null || value == DPixel.NaP) {
                receiver.value = ""
            } else {
                receiver.value = value.value.format(1, hideZeroDecimals = true)
            }
        }

        if (isInit) {
            receiver.addEventListener("input", {
                var rawInput = receiver.value

                while (rawInput.count { it == '.' } > 1) {
                    rawInput = rawInput.substringBeforeLast('.')
                }

                val wellFormattedInput = rawInput.replace("[^0-9.]".toRegex(), "")

                if (wellFormattedInput != receiver.value) {
                    receiver.value = wellFormattedInput
                }

                val value = if (wellFormattedInput.isEmpty()) null else wellFormattedInput.toDouble()

                if (value != b.value?.value) {
                    b.setValue(value?.let { DPixel(it) } ?: DPixel.NaP, true)
                }
            })

            receiver.pattern = "[0-9]*\\.[0-9]*"
            receiver.style.outline = "none"
        }
    }
}