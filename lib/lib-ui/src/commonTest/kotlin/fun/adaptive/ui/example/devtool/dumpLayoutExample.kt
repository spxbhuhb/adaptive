package `fun`.adaptive.ui.example.devtool

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.devtool.dumpLayoutButton
import `fun`.adaptive.ui.testing.LayoutTraceContext

@Adaptive
fun dumpLayoutExample() {
    column { // this fragment won't be in the dump

        localContext(LayoutTraceContext) {
            text("Hello World!") // this fragment will be in the dump
        }

        dumpLayoutButton() // this fragment won't be in the dump
    }
}