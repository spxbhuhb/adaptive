package `fun`.adaptive.grove.ufd

import `fun`.adaptive.adat.encodeToJsonString
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.grove.generated.resources.*
import `fun`.adaptive.grove.sheet.SheetViewBackend
import `fun`.adaptive.grove.sheet.fragment.sheet
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.grove.ufd.app.GroveUdfModuleMpw.Companion.udfModule
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.mpw.MultiPaneTheme.Companion.DEFAULT
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall

@Adaptive
fun ufdCenter(): AdaptiveFragment {

    // FIXME hackish set of focusedSheet
    val viewBackend = fragment().firstContext<SheetViewBackend>().also { fragment().udfModule.focusedSheet.value = it }

    grid {
        rowTemplate(DEFAULT.paneHeaderHeightDp, 1.fr)

        row {
            maxSize .. borderBottom(DEFAULT.toolBorderColor) .. spaceBetween
            onKeyDown { it.preventDefault(); viewBackend.onKeyDown(it.keyInfo !!, it.modifiers) }

            row {
                alignSelf.startCenter

                for (action in actions) {
                    action(action, viewBackend)
                }
            }

            row {
                multiplier(viewBackend)
            }

            row {
                actionIcon(Graphics.pest_control, theme = tableIconTheme) .. onClick {
                    viewBackend.snapshot.encodeToJsonString()
                }
            }
        }

        sheet()
    }

    return fragment()
}

class SheetAction(
    val icon: GraphicsResourceSet,
    val tooltip: String,
    val onClick: () -> SheetOperation
)

val actions = listOf(
    SheetAction(Graphics.undo, Strings.undoToolTip) { Undo() },
    SheetAction(Graphics.redo, Strings.redoToolTip) { Redo() },
    SheetAction(Graphics.stack_group, Strings.groupToolTip) { Group() },
    SheetAction(Graphics.stack_off, Strings.ungroupToolTip) { Ungroup() }
)

@Adaptive
private fun action(sheetAction: SheetAction, controller: SheetViewBackend) {
    actionIcon(sheetAction.icon, tooltip = sheetAction.tooltip, theme = denseIconTheme) .. onClick { controller += sheetAction.onClick() }
}

@Adaptive
fun multiplier(controller: SheetViewBackend) {
    val multiplier = observe { controller.multiplierStore }

    if (multiplier > 1) {
        text("Next keyboard move will be $multiplier pixels") .. textSmall .. semiBoldFont .. textColors.onSurfaceAngry .. alignSelf.bottom
    }
}