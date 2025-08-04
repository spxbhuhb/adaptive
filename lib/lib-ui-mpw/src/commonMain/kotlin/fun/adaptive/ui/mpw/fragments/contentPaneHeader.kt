package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.document.ui.direct.h2
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.label.uuidLabel
import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.AvValue

@Adaptive
fun contentPaneHeader(
    title: String,
    uuid: UUID<*>? = null,
    value: AvValue<*>? = null,
    _fixme_adaptive_content: @Adaptive () -> Unit
) : AdaptiveFragment {
    row {
        maxWidth .. spaceBetween

        column {
            h2(title)

            if (uuid != null) uuidLabel { uuid }

            if (value != null) {
                valueBadges(value)
            }
        }

        row {
            gap { 16.dp } .. paddingTop { 4.dp }
            _fixme_adaptive_content()
        }
    }

    return fragment()
}