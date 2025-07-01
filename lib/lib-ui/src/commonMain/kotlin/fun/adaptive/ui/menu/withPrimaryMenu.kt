package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.icon.icon

@Adaptive
fun <T> withPrimaryMenu(
    viewBackend: MenuViewBackend<T>,
    @Adaptive
    _fixme_content: () -> Unit
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