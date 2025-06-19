package `fun`.adaptive.sandbox.recipe.ui.input.number

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.sandbox.support.examplePane
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.InputTheme
import `fun`.adaptive.ui.input.inputHint
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun commonIntInputExample(): AdaptiveFragment {

    column {
        gap { 16.dp }
        noUnitNoRadix()
        noUnitRadix()
    }

    return fragment()
}

@Adaptive
private fun noUnitNoRadix() {
    val intBackend = valueFrom { intInputBackend(123).apply { label = "Non-nullable" } }
    val nullableIntBackend = valueFrom { intInputBackend(123).apply { label = "Nullable"; isNullable = true } }

    examplePane(
        "No radix",
        """
            * non-nullable considers empty input as invalid
            * nullable considers empty input as null
        """.trimIndent()
    ) {
        column {
            gap { 16.dp }

            row {
                gap { 16.dp }
                intInput(intBackend) .. width { 200.dp }
                inputHint("Value: ${intBackend.inputValue}") .. alignSelf.bottom
            }

            row {
                gap { 16.dp }
                intInput(nullableIntBackend) .. width { 200.dp }
                inputHint("Value: ${nullableIntBackend.inputValue}") .. alignSelf.bottom
            }
        }
    }
}


@Adaptive
private fun noUnitRadix() {
    val radix16NoRadix10Backend = valueFrom {
        intInputBackend(123).apply {
            label = "Radix 16, not showing radix 10"
            radix = 16
        }
    }

    val radix16WithRadix10Backend = valueFrom {
        intInputBackend(123).apply {
            label = "Radix 16, with radix 10"
            radix = 16
            showRadix10 = true
        }
    }

    examplePane(
        "Radix",
        """
            **These are non-nullable inputs.**
            * set `radix` to 16 to use hex notation
            * set `showRadix10` to show the decimal value at the bottom
        """.trimIndent()
    ) {
        column {
            gap { 16.dp }

            row {
                gap { 16.dp }

                intInput(radix16NoRadix10Backend) .. width { 200.dp }
                inputHint("Value: ${radix16NoRadix10Backend.inputValue}") .. alignSelf.bottom
            }

            row {
                gap { 16.dp }

                intInput(radix16WithRadix10Backend) .. width { 200.dp }
                box {
                    // this box positions the value next to the actual input, compensating for displayed radix
                    alignSelf.bottom
                    paddingBottom { InputTheme.DEFAULT.inputHintLineHeight + InputTheme.DEFAULT.inputHintPadding }

                    inputHint("Value: ${radix16WithRadix10Backend.inputValue}")
                }
            }
        }
    }
}