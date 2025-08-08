package `fun`.adaptive.doc.example.layout.box

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.alignItems
import `fun`.adaptive.ui.api.alignSelf
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.borders

@Adaptive
fun growingChild() {
    var current = 20.dp

    box {
        borders.outline
        size(400.dp, 200.dp)
        alignItems.endBottom

        text("Click here to grow the blue box") ..
            alignSelf.topCenter ..
            noSelect ..
            onClick { current = current + 2.dp }

        box {
            size(current, current)
            backgrounds.infoSurface
        }
    }
}