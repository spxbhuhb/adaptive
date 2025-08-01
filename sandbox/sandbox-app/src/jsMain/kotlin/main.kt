/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.nop
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.lib.util.log.CollectedLogData
import `fun`.adaptive.lib.util.log.CollectedLogItem
import `fun`.adaptive.lib.util.log.CollectingLogger
import `fun`.adaptive.log.LoggerFactory
import `fun`.adaptive.log.defaultLoggerFactory
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.CookbookFragmentFactory
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.handle.handle
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.Position
import `fun`.adaptive.ui.instruction.layout.SizeStrategy
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.snackbar.infoNotification
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    sandboxMain()
}

class Option(
    val text: String,
    val icon: GraphicsResourceSet
)

fun sandboxMain() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
//        cookbookCommon()
//        groveRuntimeCommon()
//        chartCommon()

        commonMainStringsStringStore0.load()
        `fun`.adaptive.cookbook.generated.resources.commonMainStringsStringStore0.load()

        val localBackend = backend {
//            auto()
//            worker { SnackbarManager() }
        }

        val collectedLogData = CollectedLogData()
        defaultLoggerFactory = LoggerFactory { CollectingLogger(it, collectedLogData) }

        val rootUiLogger = getLogger("UI root")

        try {
            browser(
                CanvasFragmentFactory,
                SvgFragmentFactory,
                LibFragmentFactory,
                CookbookFragmentFactory,
                backend = localBackend
            ) { adapter ->

                with(adapter.defaultTextRenderData) {
                    fontName = "Open Sans"
                    fontSize = 16.sp
                    fontWeight = 300
                }

                // adapter.traceWithContext = true

                var position = Position(0.dp, 0.dp)
                val sizing = SizeStrategy(null, null, 0.dp, 100.dp, 0.dp, 100.dp)

                row {
                    column {
                        for (i in 0 .. 100) {
                            box(if (i % 2 == 0) backgrounds.friendly else nop) {
                                width { 30.dp }
                                height { 10.dp }
                            }
                        }
                    }

//                    localContext(FragmentTraceContext()) {
//                        cellBox(
//                            cells = listOf(
//                                CellDef(null, 100.dp),
//                                CellDef(null, 100.dp)
//                            )
//                        ) {
//                            text("Hello") .. backgrounds.infoSurface .. maxWidth
//                            text("World!") .. backgrounds.warningSurface .. maxWidth
//                        } .. width { 300.dp } .. backgrounds.friendlyOpaque .. gap { 16.dp }
//                    }

                    box {
                        size(200.dp) .. borders.outline .. margin { 16.dp }

                        handle(position, { newPosition -> position = newPosition.coerce(sizing) }) {
                            box {
                                size(20.dp) .. backgrounds.angry
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            rootUiLogger.error(ex)
        }
    }
}

@Adaptive
fun collectedLog(data: CollectedLogData) {
    val observed = observe { data }

    box {
        maxSize .. zIndex { 100000 } .. noPointerEvents
        column {
            enablePointerEvents
            height { 400.dp } .. verticalScroll .. alignSelf.bottom .. maxWidth

            submitButton("Clear") .. onClick { data.clear() }
            for (item in observed.items) {
                collectedLogItem(item)
            }
        }
    }

}


@Adaptive
fun collectedLogItem(item: CollectedLogItem) {

    val lines = item.message.split("\n")

    column {
        row {
            maxWidth .. gap { 16.dp } .. height { 22.dp }

            text(item.time)
            text(item.logger)
            text(item.level)
            if (lines.size < 2) text(item.message)
        }
        if (lines.size > 1) {
            for (line in lines) {
                text(line) .. paddingLeft { 40.dp }
            }
        }
        if (item.exception != null) {
            text(item.exception)
        }
    }

}