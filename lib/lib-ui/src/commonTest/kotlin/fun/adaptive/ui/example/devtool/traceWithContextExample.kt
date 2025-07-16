package `fun`.adaptive.ui.example.devtool

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.FragmentTraceContext
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.text

@Adaptive
fun traceWithContextExample() {
    adapter().traceWithContext = true
    localContext(FragmentTraceContext()) { // all fragments inside this context will be traced
        column {
            text("Hello World!")
        }
    }
}