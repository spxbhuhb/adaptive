/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package `fun`.adaptive.grove.ufd

import adaptive_grove.generated.resources.*
import `fun`.adaptive.adat.encodeToJson
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.value.adaptiveValue
import `fun`.adaptive.grove.sheet.SheetViewController
import `fun`.adaptive.grove.sheet.fragment.sheet
import `fun`.adaptive.grove.sheet.operation.*
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.resource.string.Strings
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.colors
import `fun`.adaptive.ui.theme.textColors
import `fun`.adaptive.ui.theme.textSmall
import `fun`.adaptive.utility.debug

@Adaptive
fun ufdMain() {

    val controller = SheetViewController(false, true, true)
    val ufdContext = UfdContext()

    grid {
        colTemplate(200.dp, 200.dp, 1.fr, 400.dp)
        maxSize

        palette(ufdContext)

        descendants(controller)

        grid {
            rowTemplate(udfTheme.headerHeight, 1.fr)

            row {
                maxWidth .. borderBottom(colors.outline) .. spaceBetween
                onKeydown { controller.onKeyDown(it.keyInfo !!, it.modifiers) }

                row {
                    for (action in actions) {
                        action(action, controller)
                    }
                }

                row {
                    multiplier(controller)
                }

                row {
                    actionIcon(Graphics.pest_control, theme = tableIconTheme) .. onClick {
                        controller.snapshot.encodeToJson().debug()
                    }
                }
            }

            sheet(controller)
        }

        instructions(controller)
    }

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
    actionIcon(sheetAction.icon, tooltip = sheetAction.tooltip, theme = tableIconTheme) .. onClick { controller += sheetAction.onClick() }
}

@Adaptive
fun multiplier(controller: SheetViewController) {
    val multiplier = adaptiveValue { controller.multiplierStore }

    if (multiplier != null && multiplier > 1) {
        text("Next keyboard move will be $multiplier pixels") .. textSmall .. semiBoldFont .. textColors.onSurfaceAngry .. alignSelf.bottom
    }
}