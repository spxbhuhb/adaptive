package `fun`.adaptive.ui.datetime.theme

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.*

class DatetimeTheme(
    daySize: DPixel = 40.dp
) {

    val datePickerContainer = instructionsOf(
        size(318.dp, 412.dp),
        paddingHorizontal { 16.dp },
        paddingTop { 8.dp },
        backgrounds.surfaceVariant,
        cornerRadius(16.dp),
        colTemplate(1.fr),
        rowTemplate(64.dp, 1.fr, 36.dp)
    )

    val datePickerMonthAndYear = instructionsOf(
        maxSize,
        colTemplate(1.fr, 1.fr),
        alignItems.center
    )

    val datePickerInner = instructionsOf(

    )

    val datePickerActionsContainer = instructionsOf(
        maxWidth,
        alignItems.end,
        gap { 16.dp },
        paddingRight { 16.dp }
    )

    val datePickerActionText = instructionsOf(noSelect, textMedium)

    val dayListGrid = instructionsOf(
        colTemplate(daySize repeat 7),
        rowTemplate(daySize + 4.dp, extend = daySize),
        gap { 1.dp }
    )

    val dayBoxBase = instructionsOf(
        size(daySize, daySize),
        cornerRadius(daySize / 2.0),
        alignItems.center
    )

    val dayBoxSelected = dayBoxBase + backgrounds.friendly
    val dayBoxToday = dayBoxBase + borders.primary
    val dayBoxMarked = dayBoxBase + backgrounds.primary

    fun dayBoxInstructions(
        inMonth: Boolean,
        marked: Boolean,
        today: Boolean,
        selected: Boolean
    ): AdaptiveInstruction =
        when {
            selected -> dayBoxSelected
            today -> dayBoxToday
            marked -> dayBoxMarked
            inMonth -> dayBoxBase
            else -> dayBoxBase
        }

    val dayHeader: AdaptiveInstruction = instructionsOf(
        textColors.onSurface,
        semiBoldFont,
        noSelect
    )

    val dayTextBase = instructionsOf(noSelect, textMedium)

    val daySelected = dayTextBase + textColors.onSelected
    val dayToday = dayTextBase + textColors.primary
    val dayMarked = dayTextBase + textColors.onSurfaceFriendly
    val dayThisMonth = dayTextBase + textColors.onSurface
    val dayOtherMonth = dayTextBase + textColors.onSurfaceVariant

    fun dayInstructions(
        inMonth: Boolean,
        marked: Boolean,
        today: Boolean,
        selected: Boolean
    ): AdaptiveInstruction =
        when {
            selected -> daySelected
            today -> dayToday
            marked -> dayMarked
            inMonth -> dayThisMonth
            else -> dayOtherMonth
        }

    // FIXME list item styles are a mess, should use the same in every theme

    val listItemContainerBase = instructionsOf(
        maxWidth,
        height(48.dp),
        alignItems.startCenter,
        colTemplate(48.dp, 1.fr),
        cornerRadius(2.dp)
    )

    val listItemContainerHover = listItemContainerBase + backgroundColor(colors.primary.opaque(0.2f))
    val listItemContainerSelected = listItemContainerBase + backgroundColor(colors.primary.opaque(0.1f))

    fun listItemContainer(hover: Boolean, selected: Boolean) =
        when {
            hover -> listItemContainerHover
            selected -> listItemContainerSelected
            else -> listItemContainerBase
        }

    val listItemIconBase = instructionsOf(
        alignSelf.center,
        gridCol(1)
    )

    val listItemIconHover = listItemIconBase
    val listItemIconSelected = listItemIconBase

    fun listItemIcon(hover: Boolean, selected: Boolean) =
        when {
            hover -> listItemIconHover
            selected -> listItemIconSelected
            else -> listItemIconBase
        }

    val listItemTextBase = instructionsOf(
        noSelect,
        gridCol(2)
    )

    val listItemTextHover = listItemTextBase
    val listItemTextSelected = listItemTextBase

    fun listItemText(hover: Boolean, selected: Boolean) =
        when {
            hover -> listItemTextHover
            selected -> listItemTextSelected
            else -> listItemTextBase
        }

}