package `fun`.adaptive.doc.example.statusInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.input.status.statusInputSingle
import `fun`.adaptive.ui.input.status.statusInputSingleBackend
import `fun`.adaptive.ui.instruction.dp

/**
 * # Toggle a single status
 *
 * - label is **NOT** set automatically
 * - use backend builder to set it
 */
@Adaptive
fun statusInputSingleExample(): AdaptiveFragment {

    val viewBackend = statusInputSingleBackend(emptySet(), "disabled") {
        label = "Disabled"
    }

    statusInputSingle(viewBackend) .. width { 200.dp }

    return fragment()
}