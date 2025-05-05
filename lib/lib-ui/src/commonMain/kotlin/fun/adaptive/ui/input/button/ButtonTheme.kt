package `fun`.adaptive.ui.input.button

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgHeight
import `fun`.adaptive.graphics.svg.api.svgWidth
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.*

/**
 * Buttons have two containers, an inner and an outer. This is used to implement the
 * IntelliJ style surrounding which cannot be done with a single border.
 *
 * The margin on normal and disabled variants compensates for the space reserved for
 * the focus border of the submit and danger variants. This is necessary to arrange
 * the buttons without paying attention to the variant.
 */
class ButtonTheme : AbstractTheme() {

    // --------------------------------------------------------------------------------
    // Container
    // --------------------------------------------------------------------------------

    val innerContainerBase = instructionsOf(
        gap { 6.dp },
        inputCornerRadius,
        alignItems.center,
        height { inputHeightDp },
        paddingHorizontal { 16.dp }
    )

    val outerContainerBase = instructionsOf(
        padding { 3.dp }
    )

    // Normal

    val normalInnerContainer = instructionsOf(
        innerContainerBase,
        tabIndex { 0 },
        backgrounds.surface,
        border(colors.outline, 1.dp),
        padding(1.dp, 17.dp, 1.dp, 17.dp),
    )

    val focusNormalInnerContainer = instructionsOf(
        innerContainerBase,
        tabIndex { 0 },
        backgrounds.surface,
        border(colors.focusColor, 2.dp)
    )

    // Disabled

    val disabledInnerContainer = instructionsOf(
        innerContainerBase,
        backgrounds.surfaceVariant,
        border(colors.outline, 1.dp)
    )

    // Submit

    val submitOuterContainer = instructionsOf(
        padding(3.dp)
    )

    val submitInnerContainer = instructionsOf(
        innerContainerBase,
        tabIndex { 0 },
        backgrounds.focusSurface
    )

    val focusSubmitOuterContainer = instructionsOf(
        padding(1.dp),
        border(colors.focusColor, 2.dp),
        cornerRadius { 6.dp }
    )

    val focusSubmitInnerContainer = instructionsOf(
        innerContainerBase,
        tabIndex { 0 },
        backgrounds.focusSurface
    )

    // Danger

    val dangerOuterContainer = instructionsOf(
        padding(3.dp)
    )

    val dangerInnerContainer = instructionsOf(
        innerContainerBase,
        tabIndex { 0 },
        backgrounds.failSurface
    )

    val focusDangerOuterContainer = instructionsOf(
        padding(1.dp),
        border(colors.fail, 2.dp),
        cornerRadius { 6.dp }
    )

    val focusDangerInnerContainer = instructionsOf(
        innerContainerBase,
        tabIndex { 0 },
        backgrounds.failSurface
    )

    // --------------------------------------------------------------------------------
    // Text
    // --------------------------------------------------------------------------------

    val textBase = instructionsOf(
        fontSize(13.sp),
        buttonFont,
        noSelect
    )

    val disabledText = instructionsOf(
        textBase,
        textColors.onSurfaceVariant,
        paddingTop(2.dp)
    )

    val focusDangerText = instructionsOf(
        textBase,
        textColors.onFailSurface,
        paddingTop(2.dp)
    )

    val focusSubmitText = instructionsOf(
        textBase,
        textColors.onFocusSurface,
        paddingTop(2.dp)
    )

    val focusNormalText = instructionsOf(
        textBase,
        textColors.onSurface,
        paddingTop(2.dp) // FIXME replace manual placing with baseline
    )

    val dangerText = focusDangerText
    val submitText = focusSubmitText
    val normalText = focusNormalText

    // --------------------------------------------------------------------------------
    // Icon
    // --------------------------------------------------------------------------------

    val iconBase = instructionsOf(
        size(18.dp, 18.dp),
        svgWidth(18.dp),
        svgHeight(18.dp),
        noSelect
    )

    val disabledIcon = instructionsOf(
        iconBase,
        iconColors.onSurface
    )

    val focusDangerIcon = instructionsOf(
        iconBase,
        iconColors.onFailSurface
    )

    val focusSubmitIcon = instructionsOf(
        iconBase,
        iconColors.onFocusSurface
    )

    val focusNormalIcon = instructionsOf(
        iconBase,
        iconColors.onSurface
    )

    val dangerIcon = focusDangerIcon
    val submitIcon = focusSubmitIcon
    val normalIcon = focusNormalIcon

    companion object {
        val default = ButtonTheme()
    }

}