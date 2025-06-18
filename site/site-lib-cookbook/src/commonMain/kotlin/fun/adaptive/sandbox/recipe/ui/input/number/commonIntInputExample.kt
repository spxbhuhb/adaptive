package `fun`.adaptive.sandbox.recipe.ui.input.number

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.input.integer.intInput
import `fun`.adaptive.ui.input.integer.intInputBackend
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun commonIntInputExample(): AdaptiveFragment {
    val intBackend = intInputBackend(123)

    column {
        maxHeight .. verticalScroll .. padding { 16.dp } .. gap { 16.dp }

        h2("No unit variants")

        text("Nullable value: ${intBackend.value}")
        text("Non-nullable value: $intBackend")

        intInput(intBackend) .. width { 200.dp }
        intInput(intBackend) .. width { 200.dp }
    }

    return fragment()
}