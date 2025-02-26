package old

import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.dropShadow
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.*

val white = color(0xffffffu)
val gray = color(0x606060u)

val lightTextColor = textColor(0xffffffu)
val darkTextColor = textColor(0x2E2E2Eu)

val lightBackground = backgroundColor(0xffffffu)
val darkBackground = backgroundColor(0x2E2E2Eu)

val titleLarge = fontSize(36.sp)
val titleMedium = fontSize(20.sp)
val titleSmall = fontSize(16.sp)

val shadow = dropShadow(color(0xc0c0c0u), 3.dp, 3.dp, 3.dp)