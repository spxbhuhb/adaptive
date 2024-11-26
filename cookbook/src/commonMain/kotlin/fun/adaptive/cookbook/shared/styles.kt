package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svgFill
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.dropShadow
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.leftToRightGradient
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.marginTop
import `fun`.adaptive.ui.api.noBorder
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingHorizontal
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.theme.ThemeBackgrounds
import `fun`.adaptive.ui.theme.ThemeTextColors

val ThemeBackgrounds.red
    get() = backgroundColor(0xff0000)

val ThemeBackgrounds.green
    get() = backgroundColor(0x00ff00)

val ThemeBackgrounds.blue
    get() = backgroundColor(0x0000ff)

val ThemeBackgrounds.purple
    get() = backgroundColor(0xff00ff)

val ThemeBackgrounds.cyan
    get() = backgroundColor(0x00ffff)

val ThemeBackgrounds.yellow
    get() = backgroundColor(0xffff00)

val ThemeTextColors.white
    get() = textColor(0xffffff)

fun colors(active: Boolean = false, hover: Boolean = false) =
    when {
        active -> primaryStyles
        hover -> hoverStyles
        else -> normalStyles
    }

val primaryBackground = backgroundColor(0x6259CE)
val primaryText = textColor(0xffffffu)
val primaryIcon = svgFill(0xffffffu)

val hoverBackground = backgroundColor(0x9B8CFFu)
val hoverText = textColor(0xffffffu)
val hoverIcon = svgFill(0xffffffu)

val normalBackground = backgroundColor(0xffffffu)
val normalText = textColor(0x0)
val normalIcon = svgFill(0x0)

val primaryStyles = instructionsOf(primaryBackground, primaryText, primaryIcon)
val hoverStyles = instructionsOf(hoverBackground, hoverText, hoverIcon)
val normalStyles = instructionsOf(normalBackground, normalText, normalIcon)

val black = color(0x000000u)
val white = color(0xffffffu)
val lightGreen = color(0xA0DE6Fu)
val mediumGreen = color(0x53C282u)
val lightGray = color(0xd8d8d8u)
val mediumGray = color(0x8A8A8Fu)
val darkGray = color(0x666666u)
val purple = color(0xA644FFu)

val f16 = fontSize(16.sp)
val f12 = fontSize(12.sp)

val lightTextColor = textColor(0xffffffu)
val darkTextColor = textColor(0x2E2E2Eu)

val lightBackground = backgroundColor(0xffffffu)
val darkBackground = backgroundColor(0x2E2E2Eu)

val titleLarge = fontSize(36.sp)
val titleMedium = fontSize(20.sp)
val titleSmall = fontSize(16.sp)

val bodySmall = fontSize(12.sp)

val shadow = dropShadow(color(0xc0c0c0u), 3.dp, 3.dp, 3.dp)

val cornerRadius2 = cornerRadius(2.dp)
val cornerRadius4 = cornerRadius(4.dp)
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

val inputErrorContainer = instructionsOf(
    marginTop { 4.dp },
    border(color(0xff0000), 1.dp),
    backgroundColor(0xFFF8F8u),
    cornerRadius8,
    padding { 8.dp },
    zIndex(2000)
)

val inputErrorText = instructionsOf(
    bodySmall,
    textColor(0xff0000)
)

val blackBackground = backgroundColor(black)
val greenGradient = leftToRightGradient(lightGreen, mediumGreen)
val cornerRadius = cornerRadius(8.dp)

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

val smallScreen = instructionsOf(
    size(377.dp, 814.dp),
    border(lightGray, 1.dp),
    paddingHorizontal { 32.dp }
)

val largeScreen = instructionsOf(
    size(1400.dp, 814.dp),
    border(lightGray, 1.dp),
    paddingHorizontal { 32.dp }
)