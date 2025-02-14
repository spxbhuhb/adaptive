/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.backend.backend
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.canvas.CanvasFragmentFactory
import `fun`.adaptive.graphics.svg.SvgFragmentFactory
import `fun`.adaptive.grove.api.GroveRuntimeFragmentFactory
import `fun`.adaptive.grove.groveRuntimeCommon
import `fun`.adaptive.sandbox.commonMainStringsStringStore0
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.browser
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.PopupAlign
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

val popupStyles =
    paddingVertical { 4.dp } ..
        paddingHorizontal { 12.dp } ..
        border(colors.outline, 1.dp) ..
        cornerRadius(4.dp) ..
        backgroundColor(colors.onSurfaceFriendly.opaque(0.3f))

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

            box {
                maxSize

                popupExample { alignment, size ->
                    position(100.dp, 200.dp)

                    hoverPopup {
                        popupStyles .. alignment .. size

                        // this makes it possible to edit values even when the popup is centered
                        // for most popups this might be a bad idea
                        noPointerEvents

                        text("${alignment.horizontal} ${alignment.vertical}")
                    }
                }

                popupExample { alignment, size ->
                    position(100.dp, 700.dp)

                    primaryPopup {
                        popupStyles .. alignment .. size .. toggle

                        // this makes it possible to edit values even when the popup is centered
                        // for most popups this might be a bad idea
                        noPointerEvents

                        text("Primary - ${alignment.horizontal} ${alignment.vertical}")
                    }

                    contextPopup {
                        popupStyles .. alignment .. size .. toggle

                        // this makes it possible to edit values even when the popup is centered
                        // for most popups this might be a bad idea
                        noPointerEvents

                        text("Secondary - ${alignment.horizontal} ${alignment.vertical}")
                    }
                }

                box {

                }
            }

        }.also {
            snapshots.toList().forEach { println(it) }
        }
    }
}

@Adaptive
fun popupExample(
    vararg instructions: AdaptiveInstruction,
    @Adaptive
    popupBuilder: (PopupAlign, AdaptiveInstructionGroup) -> Unit
): AdaptiveFragment {
    var alignment = popupAlign.aboveBefore

    var popupWidth = Double.NaN
    var popupHeight = Double.NaN
    var maxWidth = false
    var maxHeight = false

    var popupSize = makeSizeInstructions(popupWidth, popupHeight, maxWidth, maxHeight)

    box(instructions()) {
        size(400.dp, 400.dp) .. padding { 8.dp } .. border(colors.onSurfaceFriendly, 8.dp)

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
                row {
                    paddingTop { 8.dp } .. gap { 8.dp } .. alignItems.center
                    editor { maxWidth }
                    text("max") .. textSmall
                }
            }
            column {
                text("Height") .. textSmall
                editor { popupHeight } .. width { 96.dp }
                row {
                    paddingTop { 8.dp } .. gap { 8.dp } .. alignItems.center
                    editor { maxHeight }
                    text("max") .. textSmall
                }
            }
        }

        text("click the checkboxes to change alignment") .. alignSelf.center .. textSmall

        popupBuilder(alignment, popupSize)
    }

    return fragment()
}

fun makeSizeInstructions(width: Double, height: Double, mw: Boolean, mh: Boolean): AdaptiveInstructionGroup {
    val out = mutableListOf<AdaptiveInstruction>()

    when {
        mw -> out += maxWidth
        ! width.isNaN() -> out += width(width.dp)
    }

    when {
        mh -> out += maxHeight
        ! height.isNaN() -> out += height(height.dp)
    }

    return AdaptiveInstructionGroup(out)
}