package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.cookbook.components.quickFilter
import `fun`.adaptive.cookbook.components.sidebar
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
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.utility.fourRandomInt
import kotlin.math.abs

@Adaptive
fun iotMain() {
    column {
        maxHeight .. verticalScroll .. backgroundColor(0xFAFAFA)

        flowBox {
            maxWidth .. gap { 16.dp }

            sidebar()
            column {
                gap(8.dp)
                thermostats()
            }
        }
    }
}