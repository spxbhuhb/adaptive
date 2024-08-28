package `fun`.adaptive.cookbook.components

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun componentsMain() {
    column {
        maxHeight .. verticalScroll .. backgroundColor(0xFAFAFA)

        flowBox {
            maxWidth .. gap { 16.dp }

            sidebar()
            quickFilter()
        }
    }
}