/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.hoverPopup
import `fun`.adaptive.ui.api.marginBottom
import `fun`.adaptive.ui.api.marginLeft
import `fun`.adaptive.ui.api.marginRight
import `fun`.adaptive.ui.api.marginTop
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingVertical
import `fun`.adaptive.ui.api.popupAlign
import `fun`.adaptive.ui.api.position
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.support.snapshot.FragmentSnapshot
import `fun`.adaptive.ui.support.snapshot.snapshot
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.uiCommon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.launch

val popupStyles = paddingVertical { 4.dp } .. paddingHorizontal { 12.dp } .. border(colors.outline, 1.dp) .. cornerRadius(4.dp)

fun main() {

    CoroutineScope(Dispatchers.Default).launch {

        uiCommon()
        groveRuntimeCommon()

        commonMainStringsStringStore0.load()

        val snapshots = Channel<FragmentSnapshot>(Channel.UNLIMITED)

        browser(CanvasFragmentFactory, SvgFragmentFactory, GroveRuntimeFragmentFactory, backend = backend { }) { adapter ->

            adapter.afterClosePatchBatch = { snapshots.trySend(it.snapshot()) }

            with(adapter.defaultTextRenderData) {
                fontName = "Open Sans"
                fontSize = 16.sp
                fontWeight = 300
            }

            var alignment = popupAlign.aboveBefore

            var popupWidth = Double.NaN
            var popupHeight = Double.NaN

            var popupSize = makeSizeInstructions(popupWidth, popupHeight)

            box {
                maxSize

                box {
                    position(100.dp, 200.dp) .. size(400.dp, 400.dp) .. padding { 8.dp } .. border(colors.onSurfaceFriendly, 8.dp)

                    checkbox(alignment == popupAlign.aboveBefore) { alignment = popupAlign.aboveBefore }
                    checkbox(alignment == popupAlign.aboveStart) { alignment = popupAlign.aboveStart } .. marginLeft { 32.dp }
                    checkbox(alignment == popupAlign.aboveCenter) { alignment = popupAlign.aboveCenter } .. alignSelf.topCenter
                    checkbox(alignment == popupAlign.aboveEnd) { alignment = popupAlign.aboveEnd } .. alignSelf.end .. marginRight { 32.dp }
                    checkbox(alignment == popupAlign.aboveAfter) { alignment = popupAlign.aboveAfter } .. alignSelf.end

                    checkbox(alignment == popupAlign.belowBefore) { alignment = popupAlign.belowBefore } .. alignSelf.bottomStart
                    checkbox(alignment == popupAlign.belowStart) { alignment = popupAlign.belowStart } .. alignSelf.bottomStart .. marginLeft { 32.dp }
                    checkbox(alignment == popupAlign.belowCenter) { alignment = popupAlign.belowCenter } .. alignSelf.bottomCenter
                    checkbox(alignment == popupAlign.belowEnd) { alignment = popupAlign.belowEnd } .. alignSelf.bottomEnd .. marginRight { 32.dp }
                    checkbox(alignment == popupAlign.belowAfter) { alignment = popupAlign.belowAfter } .. alignSelf.bottomEnd

                    checkbox(alignment == popupAlign.beforeTop) { alignment = popupAlign.beforeTop } .. alignSelf.startTop .. marginTop { 32.dp }
                    checkbox(alignment == popupAlign.beforeCenter) { alignment = popupAlign.beforeCenter } .. alignSelf.startCenter
                    checkbox(alignment == popupAlign.beforeBottom) { alignment = popupAlign.beforeBottom } .. alignSelf.startBottom .. marginBottom { 32.dp }

                    checkbox(alignment == popupAlign.afterTop) { alignment = popupAlign.afterTop } .. alignSelf.endTop .. marginTop { 32.dp }
                    checkbox(alignment == popupAlign.afterCenter) { alignment = popupAlign.afterCenter } .. alignSelf.endCenter
                    checkbox(alignment == popupAlign.afterBottom) { alignment = popupAlign.afterBottom } .. alignSelf.endBottom .. marginBottom { 32.dp }

                    row {
                        position(64.dp, 64.dp) .. gap { 16.dp }
                        column {
                            text("Width") .. textSmall
                            editor { popupWidth } .. width { 96.dp }
                        }
                        column {
                            text("Height") .. textSmall
                            editor { popupHeight } .. width { 96.dp }
                        }
                    }

                    text("click the checkboxes to change alignment") .. alignSelf.center .. textSmall

                    hoverPopup {
                        popupStyles .. alignment .. popupSize
                        text("${alignment.horizontal} ${alignment.vertical}")
                    }

                }

            }

        }.also {
            snapshots.toList().forEach { println(it) }
        }
    }
}

fun makeSizeInstructions(width: Double, height: Double): AdaptiveInstructionGroup {
    val out = mutableListOf<AdaptiveInstruction>()
    if (! width.isNaN()) out += width(width.dp)
    if (! height.isNaN()) out += height(height.dp)
    return AdaptiveInstructionGroup(out)
}