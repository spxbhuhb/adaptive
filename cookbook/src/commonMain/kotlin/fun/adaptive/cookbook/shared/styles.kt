package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.common.instruction.*

val black = Color(0x000000u)
val white = Color(0xffffffu)
val lightGreen = Color(0xA0DE6Fu)
val mediumGreen = Color(0x53C282u)
val lightGray = Color(0xd8d8d8u)
val mediumGray = Color(0x8A8A8Fu)
val darkGray = Color(0x666666u)
val purple = Color(0xA644FFu)

val lightTextColor = textColor(0xffffffu)
val darkTextColor = textColor(0x2E2E2Eu)

val lightBackground = backgroundColor(0xffffffu)
val darkBackground = backgroundColor(0x2E2E2Eu)

val titleLarge = fontSize(36.sp)
val titleMedium = fontSize(20.sp)
val titleSmall = fontSize(16.sp)

val bodySmall = fontSize(12.sp)

val shadow = dropShadow(Color(0xc0c0c0u), 3.dp, 3.dp, 3.dp)

val cornerRadius8 = cornerRadius(8.dp)
val lightBorder = border(lightGray, 1.dp)

val inputStyle = instructionsOf(
    // PlaceholderColor(0x8A8A8F),
    textColor(0x000000u),
    BackgroundColor(Color(0xEFEFF4u)),
    CornerRadius(8.dp),
    Border.NONE,
    Height(44.dp),
    FontSize(17.sp),
    FontWeight.LIGHT,
    padding(left = 16.dp, right = 16.dp)
)

val blackBackground = backgroundColor(black)
val greenGradient = leftToRightGradient(lightGreen, mediumGreen)
val cornerRadius = cornerRadius(8.dp)

val textSmall = fontSize(13.sp)
val textMedium = fontSize(15.sp)
val whiteBorder = border(white)
val bold = FontWeight(700)
val smallWhiteNoWrap = instructionsOf(textColor(white), textSmall, noTextWrap)

val button = instructionsOf(
    greenGradient,
    cornerRadius,
    AlignItems.center,
    Padding(8.dp),
    Height(50.dp)
)

val bodyMedium = instructionsOf(
    //FontSize(17.sp),
    //FontName("Noto Sans"),
    textColor(0x666666u)
)

val input = instructionsOf(
    // PlaceholderColor(0x8A8A8F),
    textColor(0x000000u),
    BackgroundColor(Color(0xEFEFF4u)),
    CornerRadius(8.dp),
    Border.NONE,
    Height(44.dp),
    FontSize(17.sp),
    FontWeight.LIGHT,
    padding(left = 16.dp, right = 16.dp)
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