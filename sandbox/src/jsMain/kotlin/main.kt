/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.sandbox.snooze
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()

        browser(CanvasFragmentFactory, SvgFragmentFactory, backend = backend { }) { adapter ->

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            var items = emptyList<String>()

            column {

                draggable {
                    transferData { "hello" }
                    text(Strings.snooze)
                }

                dropTarget {

                    onDrop {
                        items = items + (it.transferData?.data as String)
                    }

                    column(size(200.dp, 200.dp), borders.outline) {
                        verticalScroll

                        for (item in items) {
                            text(item)
                        }
                    }

                }

            }
        }
    }
}