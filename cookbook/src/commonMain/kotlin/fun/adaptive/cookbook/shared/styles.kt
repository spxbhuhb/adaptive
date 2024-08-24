package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.dropShadow
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.leftToRightGradient
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.noBorder
import `fun`.adaptive.ui.api.noTextWrap
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.*

val black = color(0x000000u)
val white = color(0xffffffu)
val lightGreen = color(0xA0DE6Fu)
val mediumGreen = color(0x53C282u)
val lightGray = color(0xd8d8d8u)
val mediumGray = color(0x8A8A8Fu)
val darkGray = color(0x666666u)
val purple = color(0xA644FFu)

val lightTextColor = textColor(0xffffffu)
val darkTextColor = textColor(0x2E2E2Eu)

val lightBackground = backgroundColor(0xffffffu)
val darkBackground = backgroundColor(0x2E2E2Eu)

val titleLarge = fontSize(36.sp)
val titleMedium = fontSize(20.sp)
val titleSmall = fontSize(16.sp)

val bodySmall = fontSize(12.sp)

val shadow = dropShadow(color(0xc0c0c0u), 3.dp, 3.dp, 3.dp)

val cornerRadius8 = cornerRadius(8.dp)
val lightBorder = border(lightGray, 1.dp)

val inputStyle = instructionsOf(
    // PlaceholderColor(0x8A8A8F),
    textColor(0x000000u),
    backgroundColor(0xEFEFF4u),
    cornerRadius(8.dp),
    noBorder,
    height { 44.dp },
    fontSize { 17.sp },
    lightFont,
    padding(left = 16.dp, right = 16.dp)
)

val blackBackground = backgroundColor(black)
val greenGradient = leftToRightGradient(lightGreen, mediumGreen)
val cornerRadius = cornerRadius(8.dp)

val textSmall = fontSize(13.sp)
val textMedium = fontSize(15.sp)
val whiteBorder = border(white)
val smallWhiteNoWrap = instructionsOf(textColor(white), textSmall, noTextWrap)

val button = instructionsOf(
    greenGradient,
    cornerRadius,
    alignItems.center,
    padding(8.dp),
    height(50.dp)
)

val bodyMedium = instructionsOf(
    //FontSize(17.sp),
    //FontName("Noto Sans"),
    textColor(0x666666u)
)

var activeCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    backgroundColor(purple),
    textColor(white)
)

var inactiveCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    border(purple, 1.dp)
)

val mobileScreen = instructionsOf(
    size(377.dp, 814.dp),
    border(lightGray, 1.dp),
    paddingHorizontal { 32.dp }
)