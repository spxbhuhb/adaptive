/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.app.WsBrowserClientApplication.Companion.wsBrowserClient
import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.SandBoxClientModule
import `fun`.adaptive.backend.backend
import `fun`.adaptive.chart.app.ChartModule
import `fun`.adaptive.document.app.DocWsModule
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.lib.util.log.CollectedLogData
import `fun`.adaptive.lib.util.log.CollectedLogItem
import `fun`.adaptive.lib.util.log.CollectingLogger
import `fun`.adaptive.log.LoggerFactory
import `fun`.adaptive.log.defaultLoggerFactory
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.CookbookFragmentFactory
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonRecipe
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.generated.resources.folder
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.value.app.ValueClientModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

fun main() {
    //virtualizedMain()
    //iotMain()
    //basicAppMain()
    sandboxMain()
    // iotMain()
}

fun iotMain() {
    wsBrowserClient {
        module { LibUiClientModule() }
        module { GroveRuntimeModule() }
        module { ValueClientModule() }
        module { ChartModule() }
        module { DocWsModule() }
        module { AppMainWsModule() }
        module { SandBoxClientModule() }
    }
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

fun sandboxMain() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
//        cookbookCommon()
//        groveRuntimeCommon()
//        chartCommon()

        commonMainStringsStringStore0.load()

        val localBackend = backend {
//            auto()
//            worker { SnackbarManager() }
        }

        val collectedLogData = CollectedLogData()
        defaultLoggerFactory = LoggerFactory { CollectingLogger(it, collectedLogData) }

        val rootUiLogger = getLogger("UI root")

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

            collectedLog(collectedLogData)
        }

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


//            adapter.cookbookCommon()
//            adapter.groveRuntimeCommon()
//            adapter.chartCommon()

                //docMain()
                //chartMain()
                //svgMain()
                //treeMain()
                //contextMenuMain()
                //datePickerMain()
                //copyStore(Stuff(AioSpaceSpec()))
//            box(name("outer")) {
//                maxSize .. padding { 16.dp } .. backgrounds.friendlyOpaque
//                box(name("300")) {
//                    width { 200.dp } .. height { 300.dp } .. padding { 16.dp } .. borders.outline .. backgrounds.surface
//                    box {
//                        scroll .. maxSize
//                        tree(TreeViewModel(generate(), context = Unit, selectedFun = ::defaultSelectedFun, openWithSingleClick = true))
//                    }
//                }
//            }
                column {
                    maxSize .. padding { 16.dp } .. gap { 16.dp }//.. backgrounds.friendlyOpaque
                    //containerPlayground()
                    //selectInputPlayground()
                    //formBasicExample()
                    buttonRecipe()
                }
            }
        } catch (ex: Exception) {
            rootUiLogger.error(ex)
        }
    }
}

private fun generate(): List<TreeItem<Unit>> {
    val numRoots = Random.nextInt(1, 4)
    return List(numRoots) { generateRandomTree(it + 1, 3, null) } // Adjust depth as needed
}

private fun generateRandomTree(index: Int, depth: Int, parent: TreeItem<Unit>?): TreeItem<Unit> {
    val nodeTitle = "Item ${index.toString().toCharArray().joinToString(".")}"
    val numChildren = Random.nextInt(1, 4)

    val item = TreeItem(
        icon = Graphics.folder,
        title = nodeTitle,
        data = Unit,
        open = true,
        parent = parent
    )

    item.children = when (depth) {
        0 -> emptyList()
        else -> List(numChildren) { generateRandomTree(index * 10 + it + 1, depth - 1, item) }
    }

    return item
}

@Adaptive
fun collectedLog(data: CollectedLogData) {
    val observed = valueFrom { data }

    box {
        maxSize .. zIndex { 100000 } .. noPointerEvents
        column {
            height { 400.dp } .. verticalScroll .. alignSelf.bottom .. maxWidth
            for (item in observed.items) {
                collectedLogItem(item)
            }
        }
    }

}


@Adaptive
fun collectedLogItem(item: CollectedLogItem) {

    column {
        row {
            maxWidth .. gap { 16.dp } .. height { 22.dp }

            text(item.time)
            text(item.logger)
            text(item.level)
            text(item.message)
        }
        if (item.exception != null) {
            text(item.exception)
        }
    }

}
