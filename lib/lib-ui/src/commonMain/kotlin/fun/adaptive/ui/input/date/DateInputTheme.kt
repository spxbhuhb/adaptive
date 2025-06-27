package `fun`.adaptive.ui.input.date

import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.canvas.api.fill
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.api.borderBottom
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.instruction.DPixel
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.theme.*
import `fun`.adaptive.ui.theme.backgrounds

class DateInputTheme(
    daySize: DPixel = 40.dp
) : AbstractTheme() {

    val dropdownPopup = instructionsOf(
        popupAlign.belowStart,
        marginTop(2.dp), // FIXME replace manual margin with popup config
        marginBottom(2.dp),
        zIndex { 200 },
        cornerRadius { 8.dp },
        border(colors.onSurface, 0.5.dp),
        backgrounds.surface,
        onClick { event -> event.stopPropagation() },
        dropShadow(colors.overlay.opaque(0.3), 8.dp, 8.dp, 8.dp)
    )

    val datePickerContainer = instructionsOf(
        size(312.dp, 380.dp),
        colTemplate(1.fr),
        rowTemplate(actionLineHeightDp, 1.fr, 42.dp),
    )

    val datePickerMonthAndYear = instructionsOf(
        maxWidth,
        height { actionLineHeightDp },
        colTemplate(1.fr, 1.fr),
        alignItems.center,
        backgrounds.surfaceVariant,
        borderBottom(colors.lightOutline, 1.dp),
        cornerTopRadius(8.dp),
        paddingHorizontal { 32.dp },
    )

    var dropdownIcon = instructionsOf(
        fill(colors.onSurfaceVariant),
        size(20.dp),
        svgWidth(20.dp),
        svgHeight(20.dp),
        alignSelf.endCenter
    )

    val datePickerInner = instructionsOf(
        paddingHorizontal { 16.dp },
    )

    val datePickerActionsContainer = instructionsOf(
        maxWidth,
        spaceBetween,
        paddingHorizontal { 16.dp },
        paddingBottom { 8.dp }
    )

    val dayListGrid = instructionsOf(
        paddingVertical { 8.dp },
        colTemplate(daySize repeat 7),
        rowTemplate(daySize + 4.dp, extend = daySize),
        gap { 1.dp }
    )

    val dayBoxBase = instructionsOf(
        size(daySize, daySize),
        cornerRadius(daySize / 2.0),
        alignItems.center
    )

    val dayBoxSelected = dayBoxBase + backgrounds.selectedFocusedSurface
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

    val dayTextBase = instructionsOf(noSelect, inputFont)

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

    val listItemContainerHover = listItemContainerBase + backgroundColor(colors.primary.opaque(0.2))
    val listItemContainerSelected = listItemContainerBase + backgroundColor(colors.primary.opaque(0.1))

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

    companion object {
        var default = DateInputTheme()
    }
}