package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.marginTop
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.tabIndex
import `fun`.adaptive.ui.api.zIndex
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders

class PopupTheme {

    val inlineEditorPopup = instructionsOf(
        marginTop { 16.dp },
        backgrounds.surfaceVariant,
        borders.outline,
        padding { 16.dp },
        cornerRadius { 4.dp },
        onClick { it.stopPropagation() },
        tabIndex { 0 },
        zIndex { 10000 }
    )

    companion object {
        var default = PopupTheme()
    }
}