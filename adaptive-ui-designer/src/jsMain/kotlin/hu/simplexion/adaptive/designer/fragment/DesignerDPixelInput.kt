/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.adaptive.designer.fragment

import hu.simplexion.adaptive.foundation.AdaptiveActual
import hu.simplexion.adaptive.foundation.AdaptiveFragment
import hu.simplexion.adaptive.foundation.binding.AdaptiveStateVariableBinding
import hu.simplexion.adaptive.ui.common.AbstractCommonFragment
import hu.simplexion.adaptive.ui.common.CommonAdapter
import hu.simplexion.adaptive.ui.common.instruction.DPixel
import hu.simplexion.adaptive.utility.checkIfInstance
import hu.simplexion.adaptive.utility.format
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement

@AdaptiveActual
open class DesignerDPixelInput(
    adapter: CommonAdapter,
    parent: AdaptiveFragment,
    index: Int
) : AbstractCommonFragment<HTMLElement>(adapter, parent, index, 0, 2) {

    // 0 - instructions
    // 1 - binding

    override val receiver: HTMLInputElement =
        document.createElement("input") as HTMLInputElement

    private val binding: AdaptiveStateVariableBinding<DPixel?>
        get() = state[1].checkIfInstance()

    override fun genPatchInternal(): Boolean {

        val b = binding

        patchInstructions()


        if (haveToPatch(dirtyMask, 1 shl 1)) {
            receiver.value = b.value?.value?.format(1, hideZeroDecimals = true) ?: ""
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
                    b.setValue(value?.let { DPixel(it) }, true)
                }
            })

            receiver.pattern = "[0-9]*\\.[0-9]*"
            receiver.style.outline = "none"
        }

        return false
    }
}