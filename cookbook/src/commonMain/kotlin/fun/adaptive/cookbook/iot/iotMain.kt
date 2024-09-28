package `fun`.adaptive.cookbook.iot

import `fun`.adaptive.cookbook.components.sidebar
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr

@Adaptive
fun iotMain() {
    grid {
        maxSize .. colTemplate(314.dp, 1.fr) .. gap(16.dp)
        backgroundColor(0xFAFAFA)

        thermostats()
    }
}