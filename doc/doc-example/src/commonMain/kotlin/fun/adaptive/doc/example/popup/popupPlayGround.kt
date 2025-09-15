package `fun`.adaptive.doc.example.popup/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.doc.example.generated.resources.hoverForPopup
import `fun`.adaptive.doc.example.generated.resources.leftClickForPopup
import `fun`.adaptive.doc.example.generated.resources.popupContent
import `fun`.adaptive.doc.example.generated.resources.rightClickForPopup
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.AdaptiveInstructionGroup
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.checkbox.checkbox
import `fun`.adaptive.ui.editor.booleanEditor
import `fun`.adaptive.ui.editor.doubleEditor
import `fun`.adaptive.ui.form.AdatFormViewBackend
import `fun`.adaptive.ui.form.adatFormBackend
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.PopupAlign
import `fun`.adaptive.ui.instruction.layout.PopupAlign.Companion.absoluteCenter
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textSmall

@Adaptive
fun popupPlayground(): AdaptiveFragment {

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

@Adat
class PopupPlaygroundConfig(
    val popupWidth: Double = Double.NaN,
    val popupHeight: Double = Double.NaN,
    val maxWidth: Boolean = false,
    val maxHeight: Boolean = false,
    val alignment: PopupAlign = popupAlign.aboveBefore
) {
    fun toInstructions(): AdaptiveInstructionGroup {
        return instructionsOf(
            makeSizeInstructions(popupWidth, popupHeight, maxWidth, maxHeight),
        )
    }

    fun makeSizeInstructions(width: Double, height: Double, mw: Boolean, mh: Boolean): AdaptiveInstructionGroup {
        val out = mutableListOf<AdaptiveInstruction>()

        when {
            mw -> out += `fun`.adaptive.ui.api.maxWidth
            ! width.isNaN() -> out += width(width.dp)
        }

        when {
            mh -> out += `fun`.adaptive.ui.api.maxHeight
            ! height.isNaN() -> out += height(height.dp)
        }

        return AdaptiveInstructionGroup(out)
    }
}

fun AdatFormViewBackend<PopupPlaygroundConfig>.setAlignment(alignment: PopupAlign) {
    setFormInputValue(inputValue.copy(alignment = alignment))
}

@Adaptive
fun alignment(): AdaptiveFragment {

    val formBackend = observe { adatFormBackend(PopupPlaygroundConfig()) }
    val config = formBackend.inputValue
    val alignment = config.alignment
    
    box(instructions()) {
        size(400.dp, 400.dp) .. padding { 8.dp } .. border(colors.onSurfaceFriendly, 8.dp)

        checkbox(alignment == popupAlign.aboveBefore) { formBackend.setAlignment(popupAlign.aboveBefore) }
        checkbox(alignment == popupAlign.aboveStart) { formBackend.setAlignment(popupAlign.aboveStart) } .. marginLeft { 48.dp }
        checkbox(alignment == popupAlign.aboveCenter) { formBackend.setAlignment(popupAlign.aboveCenter) } .. alignSelf.topCenter
        checkbox(alignment == popupAlign.aboveEnd) { formBackend.setAlignment(popupAlign.aboveEnd) } .. alignSelf.end .. marginRight { 48.dp }
        checkbox(alignment == popupAlign.aboveAfter) { formBackend.setAlignment(popupAlign.aboveAfter) } .. alignSelf.end

        checkbox(alignment == popupAlign.belowBefore) { formBackend.setAlignment(popupAlign.belowBefore) } .. alignSelf.bottomStart
        checkbox(alignment == popupAlign.belowStart) { formBackend.setAlignment(popupAlign.belowStart) } .. alignSelf.bottomStart .. marginLeft { 48.dp }
        checkbox(alignment == popupAlign.belowCenter) { formBackend.setAlignment(popupAlign.belowCenter) } .. alignSelf.bottomCenter
        checkbox(alignment == popupAlign.belowEnd) { formBackend.setAlignment(popupAlign.belowEnd) } .. alignSelf.bottomEnd .. marginRight { 48.dp }
        checkbox(alignment == popupAlign.belowAfter) { formBackend.setAlignment(popupAlign.belowAfter) } .. alignSelf.bottomEnd

        checkbox(alignment == popupAlign.beforeTop) { formBackend.setAlignment(popupAlign.beforeTop) } .. alignSelf.startTop .. marginTop { 48.dp }
        checkbox(alignment == popupAlign.beforeCenter) { formBackend.setAlignment(popupAlign.beforeCenter) } .. alignSelf.startCenter
        checkbox(alignment == popupAlign.beforeBottom) { formBackend.setAlignment(popupAlign.beforeBottom) } .. alignSelf.startBottom .. marginBottom { 48.dp }

        checkbox(alignment == popupAlign.afterTop) { formBackend.setAlignment(popupAlign.afterTop) } .. alignSelf.endTop .. marginTop { 48.dp }
        checkbox(alignment == popupAlign.afterCenter) { formBackend.setAlignment(popupAlign.afterCenter) } .. alignSelf.endCenter
        checkbox(alignment == popupAlign.afterBottom) { formBackend.setAlignment(popupAlign.afterBottom) } .. alignSelf.endBottom .. marginBottom { 48.dp }

        localContext(formBackend) {
            row {
                position(64.dp, 64.dp) .. gap { 16.dp }
                column {
                    width { 96.dp }
                    doubleEditor { config.popupWidth }
                    booleanEditor { config.maxWidth }
                }
                column {
                    width { 96.dp }
                    doubleEditor { config.popupHeight }
                    booleanEditor { config.maxHeight }
                }
            }
        }

        text("click the checkboxes to change alignment") .. alignSelf.center .. textSmall
        text("alignment FLIPS when there is not enough space") .. alignSelf.center .. textSmall .. boldFont .. marginTop { 32.dp }

        hoverPopup {
            popupStyles .. alignment .. formBackend.inputValue.toInstructions()

            // this makes it possible to edit values even when the popup is centered
            // for most popups this might be a bad idea
            noPointerEvents

            text("${alignment.horizontal} ${alignment.vertical}")
        }
    }

    return fragment()
}

val popupStyles =
    paddingVertical { 4.dp } ..
        paddingHorizontal { 12.dp } ..
        border(colors.outline, 1.dp) ..
        cornerRadius(4.dp) ..
        backgroundColor(colors.onSurfaceFriendly.opaque(0.3)) ..
        zIndex { 1000 }
