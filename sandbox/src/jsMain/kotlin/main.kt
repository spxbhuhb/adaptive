/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update
import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.app.UiClientApplicationData
import `fun`.adaptive.app.WsBrowserApplication
import `fun`.adaptive.app.ws.BasicAppWsModule
import `fun`.adaptive.app.ws.WsSandBoxModule
import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.chart.ChartWsModule
import `fun`.adaptive.chart.chartCommon
import `fun`.adaptive.chart.ui.lineChart
import `fun`.adaptive.cookbook.cookbookCommon
import `fun`.adaptive.document.DocWsModule
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.GroveRuntimeModule
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.iot.IotWsModule
import `fun`.adaptive.iot.iotCommon
import `fun`.adaptive.iot.space.AioSpaceSpec
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.LibUiModule
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.input.text.textInputArea
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.label.withLabel
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.ui.uiCommon
import `fun`.adaptive.ui.workspace.Workspace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {
    //basicAppMain()
    sandboxMain()
    // iotMain()
}

fun iotMain() {
    WsBrowserApplication(
        UiClientApplicationData(),
        // modules
        LibUiModule(),
        GroveRuntimeModule<Workspace>(),
        ChartWsModule(),
        DocWsModule(),
        IotWsModule(),
        BasicAppWsModule(),
        WsSandBoxModule
    ).main()
}

fun sandboxMain() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        cookbookCommon()
        groveRuntimeCommon()
        chartCommon()
        iotCommon()

        commonMainStringsStringStore0.load()

        val localBackend = backend {
            auto()
            worker { SnackbarManager() }
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

            adapter.cookbookCommon()
            adapter.groveRuntimeCommon()
            adapter.chartCommon()

            //docMain()
            //chartMain()
            //svgMain()
            //treeMain()
            //contextMenuMain()
            //datePickerMain()
            //copyStore(Stuff(AioSpaceSpec()))
            box {
                maxSize .. padding { 16.dp }
                lineChart()
            }
        }
    }
}

@Adat
class Stuff(val spec: AioSpaceSpec)

@Adaptive
fun copyStore(stuff: Stuff) {
    val originalSpec = copyOf { stuff.spec }
    val editSpec = copyOf { stuff.spec }

    println(editSpec)

    column {
        padding { 16.dp } .. maxSize

        withLabel("Original spec") {
            textInputArea(editSpec.notes) { v -> editSpec.update(editSpec::notes, v) } .. width { 300.dp }
        }
    }
}