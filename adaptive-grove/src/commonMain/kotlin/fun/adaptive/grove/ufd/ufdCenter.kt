package `fun`.adaptive.grove.ufd

import `fun`.adaptive.adat.encodeToJsonString
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.grove.resources.*
import `fun`.adaptive.grove.sheet.SheetViewContext
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.fragment.sheet
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.denseIconTheme
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.ui.workspace.Workspace.Companion.wsContext
import `fun`.adaptive.ui.workspace.WorkspaceTheme.Companion.workspaceTheme
import `fun`.adaptive.utility.debug

@Adaptive
fun ufdCenter() : AdaptiveFragment {

    val controller = SheetViewController().also { fragment().wsContext<SheetViewContext>().focusedView.value = it }

    localContext(controller) {
        grid {
            rowTemplate(workspaceTheme.titleHeight, 1.fr)

            row {
                maxSize .. borderBottom(workspaceTheme.toolBorderColor) .. spaceBetween
                onKeydown { controller.onKeyDown(it.keyInfo !!, it.modifiers) }

                row {
                    alignSelf.startCenter

                    for (action in actions) {
                        action(action, controller)
                    }
                }

                row {
                    multiplier(controller)
                }

                row {
                    actionIcon(Graphics.pest_control, theme = tableIconTheme) .. onClick {
                        controller.snapshot.encodeToJsonString()
                    }
                }
            }

            sheet()
        }
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
private fun action(sheetAction: SheetAction, controller: SheetViewController) {
    actionIcon(sheetAction.icon, tooltip = sheetAction.tooltip, theme = denseIconTheme) .. onClick { controller += sheetAction.onClick() }
}

@Adaptive
fun multiplier(controller: SheetViewController) {
    val multiplier = valueFrom { controller.multiplierStore }

    if (multiplier > 1) {
        text("Next keyboard move will be $multiplier pixels") .. textSmall .. semiBoldFont .. textColors.onSurfaceAngry .. alignSelf.bottom
    }
}