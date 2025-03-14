package `fun`.adaptive.ui.dialog

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.rootBox
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.width

import `fun`.adaptive.ui.builtin.close
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.event.OnClose

@Adaptive
fun dialog(title : String, vararg instructions: AdaptiveInstruction, @Adaptive _fixme_adaptive_content: () -> Unit): AdaptiveFragment {

    val onClose = fragment().instructions.firstInstanceOfOrNull<OnClose>()

    rootBox {
        dialogTheme.root

        column(instructions()) {
            dialogTheme.mainContainer .. width(708.dp)

            row {
                dialogTheme.titleContainer .. size(708.dp, 68.dp)

                text(title) .. dialogTheme.titleText

                svg(Graphics.close) .. dialogTheme.titleIcon .. onClick {
                    onClose?.handler?.invoke()
                }
            }

            _fixme_adaptive_content()
        }
    }

    return fragment()
}