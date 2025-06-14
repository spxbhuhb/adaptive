/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.BasicBrowserClientApplication.Companion.basicBrowserClient
import `fun`.adaptive.ui.mpw.fragments.contentPaneHeader
import `fun`.adaptive.app.ws.SandBoxClientModule
import `fun`.adaptive.backend.backend
import `fun`.adaptive.chart.app.ChartModule
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
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.sandbox.CookbookFragmentFactory
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.sandbox.recipe.ui.input.button.buttonPlayground
import `fun`.adaptive.sandbox.recipe.ui.input.select.Option
import `fun`.adaptive.sandbox.recipe.ui.input.select.selectInputPlayground
import `fun`.adaptive.sandbox.recipe.ui.menu.contextMenuPlayground
import `fun`.adaptive.sandbox.recipe.ui.tree.treePlayground
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.badge.BadgeTheme
import `fun`.adaptive.ui.badge.BadgeTheme.Companion.badgeThemeMap
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.generated.resources.menu_book
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.input.select.selectInputBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.loading.loading
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.utility.UUID.Companion.uuid4
import `fun`.adaptive.value.AvValue
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

        val localBackend = backend {
//            auto()
//            worker { SnackbarManager() }
        }

        val collectedLogData = CollectedLogData()
        defaultLoggerFactory = LoggerFactory { CollectingLogger(it, collectedLogData) }

        val rootUiLogger = getLogger("UI root")

//        browser(
//            CanvasFragmentFactory,
//            SvgFragmentFactory,
//            LibFragmentFactory,
//            CookbookFragmentFactory,
//            backend = localBackend
//        ) { adapter ->
//
//            with(adapter.defaultTextRenderData) {
//                fontName = "Open Sans"
//                fontSize = 16.sp
//                fontWeight = 300
//            }
//
//            collectedLog(collectedLogData)
//        }


        val o = (1 .. 30).map { Option("Option $it", Graphics.menu_book) }

        val backend = selectInputBackend<Option>(null) {
            options = o
            label = "Hello"
            withSurfaceContainer = true
            toText = { it.text }
            toIcon = { it.icon }
        }

        badgeThemeMap += "success" to BadgeTheme.success
        badgeThemeMap += "info" to BadgeTheme.info
        badgeThemeMap += "warning" to BadgeTheme.warning
        badgeThemeMap += "error" to BadgeTheme.error

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
                    maxSize .. margin { 16.dp } .. padding { 16.dp } .. gap { 16.dp } .. verticalScroll //.. backgrounds.friendlyOpaque


                    treePlayground()

                    contextMenuPlayground()

                    buttonPlayground()


                    contentPaneHeader("Hello", uuid4<Any>(), AvValue(
                        statusOrNull = setOf("success", "info", "warning", "error"),
                        markersOrNull = setOf("marker1", "marker2"),
                        spec = ""
                    )) { }

                   loading(null) { height { (57 + 242).dp } }

//                    box {
//                        size(300.dp)
//
//                        selectInputList(
//                            backend
//                        ) { selectInputOptionIconAndText(it) } .. instructions()
//                    }

                    //containerPlayground()
                    selectInputPlayground()
                    //formBasicExample()
                    //treeRecipe()
                    //treeValueExample()
//                    box {
//                        maxWidth .. borders.outline .. backgrounds.surface .. height { 40.dp}
//                    }
                }
            }
        } catch (ex: Exception) {
            rootUiLogger.error(ex)
        }
    }
}

@Adaptive
fun collectedLog(data: CollectedLogData) {
    val observed = valueFrom { data }

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