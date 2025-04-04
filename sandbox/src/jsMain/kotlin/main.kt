/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.app.WsBrowserClientApplication.Companion.wsBrowserClient
import `fun`.adaptive.app.ws.AppMainWsModule
import `fun`.adaptive.app.ws.WsSandBoxModule
import `fun`.adaptive.backend.backend
import `fun`.adaptive.chart.ChartWsModule
import `fun`.adaptive.document.DocWsModule
import `fun`.adaptive.foundation.instruction.name
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.iot.app.IotWsModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.LibUiClientModule
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.builtin.folder
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.tree.TreeItem
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.ui.tree.TreeViewModel.Companion.defaultSelectedFun
import `fun`.adaptive.ui.tree.tree
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
        module { ChartWsModule() }
        module { DocWsModule() }
        module { IotWsModule() }
        module { AppMainWsModule() }
        module { WsSandBoxModule }
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

        browser(
            CanvasFragmentFactory,
            SvgFragmentFactory,
            LibFragmentFactory,
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
            box(name("outer")) {
                maxSize .. padding { 16.dp } .. backgrounds.friendlyOpaque
                box(name("300")) {
                    width { 200.dp } .. height { 300.dp } .. padding { 16.dp } .. borders.outline .. backgrounds.surface
                    box {
                        scroll .. maxSize
                        tree(TreeViewModel(generate(), context = Unit, selectedFun = ::defaultSelectedFun, openWithSingleClick = true))
                    }
                }
            }
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