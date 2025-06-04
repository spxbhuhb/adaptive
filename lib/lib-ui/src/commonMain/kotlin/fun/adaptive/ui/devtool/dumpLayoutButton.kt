package `fun`.adaptive.ui.devtool

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment.FoundationLocalContext
import `fun`.adaptive.foundation.query.firstOrNull
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.support.snapshot.SnapshotLayoutDumpVisitor
import `fun`.adaptive.ui.support.snapshot.snapshot
import `fun`.adaptive.ui.testing.LayoutTraceContext

/**
 * When clicked, dumps the layout on the console for the first [LayoutTraceContext] of the adapter.
 */
@Adaptive
fun dumpLayoutButton() {
    button("Dump Layout") .. onClick {
        val data = SnapshotLayoutDumpVisitor.VisitorData()
        adapter().firstOrNull { it is FoundationLocalContext && it.context == LayoutTraceContext }?.snapshot()?.accept(SnapshotLayoutDumpVisitor(), data)
        println(data.output)
    }
}