/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.doc.app.ExampleFragmentFactory
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.sandbox.app.generated.resources.commonMainStringsStringStore0
import `fun`.adaptive.ui.AbstractAuiAdapter
import `fun`.adaptive.ui.LibFragmentFactory
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.fragment.layout.SizingProposal
import `fun`.adaptive.ui.fragment.layout.cellbox.CellBoxArrangementCalculator
import `fun`.adaptive.ui.fragment.layout.cellbox.CellDef
import `fun`.adaptive.ui.generated.resources.account_box
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.table.TableViewBackendBuilder.Companion.tableBackend
import `fun`.adaptive.ui.table.table
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.Clock.System.now

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()

        commonMainStringsStringStore0.load()
        `fun`.adaptive.doc.example.generated.resources.commonMainStringsStringStore0.load()

        val localBackend = backend {
//            worker { SnackbarManager() }
        }

//        val collectedLogData = CollectedLogData()
//        defaultLoggerFactory = LoggerFactory { CollectingLogger(it, collectedLogData) }

        try {
            browser(
                CanvasFragmentFactory,
                SvgFragmentFactory,
                LibFragmentFactory,
                ExampleFragmentFactory,
                backend = localBackend
            ) { adapter ->
//
                with(adapter.defaultTextRenderData) {
                    fontName = "Open Sans"
                    fontSize = 16.sp
                    fontWeight = 300
                }

//                var position = Position(0.dp, 0.dp)
//                val sizing = SizeStrategy(null, null, 0.dp, 100.dp, 0.dp, 100.dp)
//
//            row {
//                column {
//                    for (i in 0 .. 100) {
//                        box(if (i % 2 == 0) backgrounds.friendly else nop) {
//                            width { 30.dp }
//                            height { 10.dp }
//                        }
//                    }
//                }

                column {
                    padding { 16.dp }
                    tableTest()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

class T(
    val friendlyId: String?,
    val name: String?,
    val currentL1: Double?
)

@Adaptive
fun tableTest() {

    val now = now()

    val backend = tableBackend {

        items = mutableListOf()
        repeat(100) { items += T("RHT-$it", "Hűtő", it.toDouble()) }

//        val currents = cellGroup("Currents")

        iconCell {
            label = "Icon"
            get = { Graphics.account_box }
            width = 32.dp
            instructions = instructionsOf(svgWidth(24.dp), svgHeight(24.dp))
        }

        stringCell {
            label = "# ID"
            get = { it.friendlyId }
            minWidth = 120.dp
            width = 120.dp
            //instructions = instructionsOf(backgrounds.friendlyOpaque, alignSelf.end)
        }

        stringCell {
            label = "Name"
            get = { it.name }
            minWidth = 300.dp
            //instructions = instructionsOf(maxWidth)
        }

        doubleCell {
            label = "L1"
            get = { it.currentL1 }
            minWidth = 100.dp
        }

        doubleCell {
            label = "L2"
            get = { it.currentL1 }
            minWidth = 100.dp
        }

        doubleCell {
            label = "L3"
            get = { it.currentL1 }
            minWidth = 100.dp
        }

        statusCell {
            label = "Status"
            get = { setOf("online") }
            minWidth = 100.dp
        }

        timeAgoCell {
            label = "Last change"
            get = { now }
            minWidth = 100.dp
        }

    }

    table(backend)
}

@Adaptive
fun table2(proposal: SizingProposal) {

    val arrangement = CellBoxArrangementCalculator(adapter() as AbstractAuiAdapter<*, *>).findBestArrangement(
        cells = listOf(
            CellDef(null, 500.dp),
            CellDef(null, 500.dp, 1.fr)
        ),
        proposal.maxWidth,
        16.0
    )

    column {
        width { proposal.maxWidth.dp } .. height { proposal.maxHeight.dp } .. verticalScroll

        for (i in 1 .. 100) {
            cellBox(arrangement = arrangement) {
                text("Hello") .. backgrounds.infoSurface .. maxWidth
                text("World!") .. backgrounds.warningSurface .. maxWidth
            } .. backgrounds.friendlyOpaque .. gap { 16.dp }
        }
    }
}


//@Adaptive
//fun collectedLog(data: CollectedLogData) {
//    val observed = observe { data }
//
//    box {
//        maxSize .. zIndex { 100000 } .. noPointerEvents
//        column {
//            enablePointerEvents
//            height { 400.dp } .. verticalScroll .. alignSelf.bottom .. maxWidth
//
//            submitButton("Clear") .. onClick { data.clear() }
//            for (item in observed.items) {
//                collectedLogItem(item)
//            }
//        }
//    }
//
//}
//
//
//@Adaptive
//fun collectedLogItem(item: CollectedLogItem) {
//
//    val lines = item.message.split("\n")
//
//    column {
//        row {
//            maxWidth .. gap { 16.dp } .. height { 22.dp }
//
//            text(item.time)
//            text(item.logger)
//            text(item.level)
//            if (lines.size < 2) text(item.message)
//        }
//        if (lines.size > 1) {
//            for (line in lines) {
//                text(line) .. paddingLeft { 40.dp }
//            }
//        }
//        if (item.exception != null) {
//            text(item.exception)
//        }
//    }
//
//}