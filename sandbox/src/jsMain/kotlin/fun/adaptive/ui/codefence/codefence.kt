package `fun`.adaptive.ui.codefence

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.borderLeft
import `fun`.adaptive.ui.api.borderRight
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.fontName
import `fun`.adaptive.ui.api.marginRight
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders
import `fun`.adaptive.ui.theme.colors

@Adaptive
fun codefence(code: String): AdaptiveFragment {

    column(instructions()) {
        borders.outline .. backgrounds.surfaceVariant

        for (line in code.lines().withIndex()) {
            row {
                box {
                    width { 64.dp } .. alignItems.endCenter .. paddingRight { 12.dp } .. borderRight(colors.outline) .. marginRight { 8.dp }
                    text(line.index) .. fontName { "Courier New" }
                }
                text(line.value) .. fontName { "Courier New" }
            }
        }
    }

    return fragment()
}