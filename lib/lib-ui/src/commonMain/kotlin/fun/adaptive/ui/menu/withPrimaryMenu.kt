package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.primaryPopup

@Adaptive
fun <T> withPrimaryMenu(
    viewBackend: MenuViewBackend<T>,
    _fixme_content: @Adaptive () -> Unit
): AdaptiveFragment {

    box {
        _fixme_content()
        primaryPopup(viewBackend) {
            column(viewBackend.theme.container, instructions()) {
                for (item in viewBackend.items) {
                    node(item, viewBackend)
                }
            }
        }
    }

    return fragment()
}