package `fun`.adaptive.app.ui.common

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.badge
import `fun`.adaptive.ui.generated.resources.info
import `fun`.adaptive.ui.generated.resources.marker
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue

@Adaptive
fun contentPaneHeader(
    title: String,
    uuid: UUID<*>? = null,
    value: AvValue<*>? = null,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    row {
        maxWidth .. spaceBetween

        column {
            h2(title)

            if (uuid != null) uuidLabel { uuid }

            if (value != null) {
                flowBox {
                    paddingTop { 6.dp } .. gap { 8.dp }
                    for (marker in value.markers.sortedBy { it }) {
                        badge(marker, Graphics.marker)
                    }
                    for (status in value.status.sortedBy { it }) {
                        badge(status, Graphics.info)
                    }
                }

            }

        }

        row {
            gap { 16.dp } .. paddingTop { 4.dp }
            _fixme_adaptive_content()
        }
    }
}