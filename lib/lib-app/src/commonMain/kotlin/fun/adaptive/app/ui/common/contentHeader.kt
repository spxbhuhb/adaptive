package `fun`.adaptive.app.ui.common

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.utility.UUID

@Adaptive
fun contentPaneHeader(
    title: String,
    uuid: UUID<*>? = null,
    @Adaptive
    _fixme_adaptive_content: () -> Unit
) {
    row {
        maxWidth .. spaceBetween

        column {
            h2(title)
            if (uuid != null) uuidLabel { uuid }
        }

        row {
            gap { 16.dp } .. paddingTop { 4.dp }
            _fixme_adaptive_content()
        }
    }
}