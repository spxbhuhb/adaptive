package `fun`.adaptive.sandbox.app.mpw.ui

import `fun`.adaptive.doc.example.mpw.mpwToolPaneExampleWrapper
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.misc.todo

@Adaptive
fun appHome(): AdaptiveFragment {

    column {
        width { 400.dp}
        todo()

        mpwToolPaneExampleWrapper()

    }

    return fragment()
}