package `fun`.adaptive.ui.virtualized

import `fun`.adaptive.backend.backend
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.ui.AuiAdapter
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun virtualizedMain() {

    CoroutineScope(Dispatchers.Default).launch {
        uiCommon()

        commonMainStringsStringStore0.load()

        val localBackend = backend { }

        browser(backend = localBackend) { adapter ->

            adapter.fragmentFactory.add("aui:virtualized") { p, i, s -> AuiVirtualized(p.adapter as AuiAdapter, p, i) }

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            val model = VirtualizationModel<Int>(
                listOf(1, 2, 3)
            )

            column {
                maxSize .. padding { 16.dp }
                text("stuff - 1")
                virtualized(model) { index, item -> text("item $item") }
                text("stuff - 2")
            }
        }
    }

}