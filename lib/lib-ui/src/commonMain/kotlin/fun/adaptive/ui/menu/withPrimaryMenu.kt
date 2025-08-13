package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.primaryPopup

@Adaptive
fun <T> withPrimaryMenu(
    viewBackend: MenuViewBackend<T>,
    content: @Adaptive () -> Unit
): AdaptiveFragment {

    box {
        content()
        primaryPopup(viewBackend) {
            menu(viewBackend)
        }
    }

    return fragment()
}