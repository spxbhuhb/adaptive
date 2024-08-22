import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.common.instruction.*

val white = Color(0xffffffu)
val gray = Color(0x606060u)

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
val lightBorder = border(gray, 1.dp)

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
