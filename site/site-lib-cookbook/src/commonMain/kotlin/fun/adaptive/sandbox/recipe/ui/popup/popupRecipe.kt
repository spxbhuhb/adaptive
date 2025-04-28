package `fun`.adaptive.sandbox.recipe.ui.popup/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.cookbook.generated.resources.hoverForPopup
import `fun`.adaptive.cookbook.generated.resources.leftClickForPopup
import `fun`.adaptive.cookbook.generated.resources.popupContent
import `fun`.adaptive.cookbook.generated.resources.rightClickForPopup
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.PopupAlign.Companion.absoluteCenter
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textSmall

@Adaptive
fun popupRecipe(): AdaptiveFragment {

    column {
        gap { 16.dp }

        box {
            text(Strings.hoverForPopup) .. noSelect
            hoverPopup {
                popupStyles .. popupAlign.afterCenter
                text(Strings.popupContent)
            }
        }

        box {
            text(Strings.leftClickForPopup) .. noSelect
            primaryPopup {
                popupStyles .. popupAlign.afterCenter
                text(Strings.popupContent)
            }
        }

        box {
            text(Strings.rightClickForPopup) .. noSelect
            contextPopup {
                popupStyles .. popupAlign.afterCenter
                text(Strings.popupContent)
            }
        }

        box {
            text(Strings.leftClickForPopup + " - absolute") .. noSelect
            primaryPopup {
                popupStyles .. absoluteCenter(false, 50.dp)
                text(Strings.popupContent)
            }
        }

        alignment()
    }

    return fragment()
}

@Adaptive
fun alignment(
    vararg instructions: AdaptiveInstruction,
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
        checkbox(alignment == popupAlign.aboveStart) { alignment = popupAlign.aboveStart } .. marginLeft { 48.dp }
        checkbox(alignment == popupAlign.aboveCenter) { alignment = popupAlign.aboveCenter } .. alignSelf.topCenter
        checkbox(alignment == popupAlign.aboveEnd) { alignment = popupAlign.aboveEnd } .. alignSelf.end .. marginRight { 48.dp }
        checkbox(alignment == popupAlign.aboveAfter) { alignment = popupAlign.aboveAfter } .. alignSelf.end

        checkbox(alignment == popupAlign.belowBefore) { alignment = popupAlign.belowBefore } .. alignSelf.bottomStart
        checkbox(alignment == popupAlign.belowStart) { alignment = popupAlign.belowStart } .. alignSelf.bottomStart .. marginLeft { 48.dp }
        checkbox(alignment == popupAlign.belowCenter) { alignment = popupAlign.belowCenter } .. alignSelf.bottomCenter
        checkbox(alignment == popupAlign.belowEnd) { alignment = popupAlign.belowEnd } .. alignSelf.bottomEnd .. marginRight { 48.dp }
        checkbox(alignment == popupAlign.belowAfter) { alignment = popupAlign.belowAfter } .. alignSelf.bottomEnd

        checkbox(alignment == popupAlign.beforeTop) { alignment = popupAlign.beforeTop } .. alignSelf.startTop .. marginTop { 48.dp }
        checkbox(alignment == popupAlign.beforeCenter) { alignment = popupAlign.beforeCenter } .. alignSelf.startCenter
        checkbox(alignment == popupAlign.beforeBottom) { alignment = popupAlign.beforeBottom } .. alignSelf.startBottom .. marginBottom { 48.dp }

        checkbox(alignment == popupAlign.afterTop) { alignment = popupAlign.afterTop } .. alignSelf.endTop .. marginTop { 48.dp }
        checkbox(alignment == popupAlign.afterCenter) { alignment = popupAlign.afterCenter } .. alignSelf.endCenter
        checkbox(alignment == popupAlign.afterBottom) { alignment = popupAlign.afterBottom } .. alignSelf.endBottom .. marginBottom { 48.dp }

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
        text("alignment FLIPS when there is not enough space") .. alignSelf.center .. textSmall .. boldFont .. marginTop { 32.dp }

        hoverPopup {
            popupStyles .. alignment .. popupSize

            // this makes it possible to edit values even when the popup is centered
            // for most popups this might be a bad idea
            noPointerEvents

            text("${alignment.horizontal} ${alignment.vertical}")
        }
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

val popupStyles =
    paddingVertical { 4.dp } ..
        paddingHorizontal { 12.dp } ..
        border(colors.outline, 1.dp) ..
        cornerRadius(4.dp) ..
        backgroundColor(colors.onSurfaceFriendly.opaque(0.3)) ..
        zIndex { 1000 }
