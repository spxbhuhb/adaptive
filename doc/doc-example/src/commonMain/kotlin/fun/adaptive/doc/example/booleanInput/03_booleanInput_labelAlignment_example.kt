package `fun`.adaptive.doc.example.booleanInput

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.input.bool.booleanInput
import `fun`.adaptive.ui.input.bool.booleanInputBackend
import `fun`.adaptive.ui.instruction.layout.PopupAlign

/**
 * # Label alignment
 *
 * - set `labelAlignment` to position the label before/after or above/below
 */
@Adaptive
fun booleanInputLabelAlignmentExample(): AdaptiveFragment {

    val viewBackend = booleanInputBackend(false).apply {
        label = "On/Off"
        labelAlignment = PopupAlign.beforeCenter
    }

    booleanInput(viewBackend)

    return fragment()
}
