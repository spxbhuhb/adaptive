package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.marker
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.value.AvValue

@Adaptive
fun valueBadges(value : AvValue<*>) : AdaptiveFragment {
    flowBox(instructions()) {
        paddingTop { 6.dp } .. gap { 8.dp }
        for (status in value.status.sortedBy { it }) {
            badge(status, useSeverity = true)
        }
        for (marker in value.markers.sortedBy { it }) {
            badge(marker, Graphics.marker)
        }
    }
    return fragment()
}