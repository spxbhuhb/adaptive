package `fun`.adaptive.cookbook.support

import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.theme.ThemeBackgrounds

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