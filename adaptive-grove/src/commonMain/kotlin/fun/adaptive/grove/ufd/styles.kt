package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.paddingVertical
import `fun`.adaptive.ui.api.spaceBetween
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors

var commonRowStyles = instructionsOf(
    height { 24.dp },
    maxWidth,
    paddingVertical { 4.dp },
    paddingHorizontal { 6.dp },
    cornerRadius(3.dp),
    alignItems.center,
    spaceBetween
)

val nonSelectedRowStyles = commonRowStyles
val selectedRowStyles = commonRowStyles + backgrounds.selected
val hoverRowStyles = commonRowStyles + backgrounds.primaryHover

fun rowStyles(selected: Boolean, hover : Boolean) =
    when {
        hover -> hoverRowStyles
        selected -> selectedRowStyles
        else -> nonSelectedRowStyles
    }

fun textStyles(selected: Boolean, hover : Boolean) =
    when {
        hover -> textColors.onPrimaryHover
        selected -> textColors.onSelected
        else -> textColors.onSurface
    }
