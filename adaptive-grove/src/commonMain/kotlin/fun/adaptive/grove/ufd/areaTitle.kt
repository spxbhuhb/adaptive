package `fun`.adaptive.grove.ufd

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.borderBottom
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun areaTitle(title: String) {
    text(title) .. maxWidth .. borderBottom(colors.outline)
}