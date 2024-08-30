package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.cookbook.components.sidebar
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun iotMain() {
    column {
        maxHeight .. verticalScroll .. backgroundColor(0xFAFAFA)

        row {
            maxWidth .. gap { 16.dp }
            sidebar()
            thermostats()
        }
    }
}