package `fun`.adaptive.ui.dialog

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.generated.resources.close
import `fun`.adaptive.ui.instruction.event.OnClose

@Adaptive
fun deprecatedDialog(
    title: String,
    theme: DialogTheme = DialogTheme.default,
    @Adaptive _fixme_adaptive_content: () -> Unit
): AdaptiveFragment {

    val onClose = fragment().instructions.firstInstanceOfOrNull<OnClose>()

    rootBox {
        theme.root

        column(instructions()) {
            theme.mainContainer

            grid {
                theme.titleContainer

                svg(Graphics.close) .. theme.titleIcon .. onClick {
                    onClose?.handler?.invoke()
                }

                text(title) .. theme.titleText

            }

            _fixme_adaptive_content()
        }
    }

    return fragment()
}