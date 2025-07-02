/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.app.ws.SandBoxClientModule
import `fun`.adaptive.backend.backend
import `fun`.adaptive.chart.app.ChartModule
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.lib.util.log.CollectedLogData
import `fun`.adaptive.lib.util.log.CollectedLogItem
import `fun`.adaptive.lib.util.log.CollectingLogger
import `fun`.adaptive.log.LoggerFactory
import `fun`.adaptive.log.defaultLoggerFactory
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.CookbookFragmentFactory
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.sandbox.recipe.ui.form.formBasicExample
import `fun`.adaptive.sandbox.recipe.ui.input.double_.doubleInputUnit
import `fun`.adaptive.sandbox.recipe.ui.input.integer.intInputRadixWithDecimalExample
import `fun`.adaptive.sandbox.recipe.ui.input.integer.intInputUnitExample
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.input.datetime.dateTimeInput
import `fun`.adaptive.ui.input.datetime.dateTimeInputBackend
import `fun`.adaptive.ui.input.double_.doubleInput
import `fun`.adaptive.ui.input.double_.doubleInputBackend
import `fun`.adaptive.ui.input.timerange.timeRangeInput
import `fun`.adaptive.ui.input.timerange.timeRangeInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.value.app.ValueClientModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    //virtualizedMain()
    //basicAppMain()
    sandboxMain()
    // iotMain()
}

fun basicAppMain() {
    basicBrowserClient {
        localTransport = true
        module { LibUiClientModule() }
        module { GroveRuntimeModule() }
        module { ValueClientModule() }
        module { ChartModule() }
        module { SandBoxClientModule() }
    }
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

                var feedBack : String = ""
                val backend = observe {
                    dateTimeInputBackend {
                        onChange = { feedBack = it.toString() }
                    }
                }

                column {
                    maxSize .. margin { 16.dp } .. padding { 16.dp } .. gap { 16.dp } .. verticalScroll .. backgrounds.friendlyOpaque

                    doubleInput(doubleInputBackend(12.3) { unit = "min" }) .. width { 100.dp} .. backgrounds.surfaceVariant
                    text("FeedBack: $feedBack")
                    //formBasicExample()
                    // quickFilterRecipe()
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