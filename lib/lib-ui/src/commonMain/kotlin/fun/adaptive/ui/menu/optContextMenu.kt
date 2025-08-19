package `fun`.adaptive.ui.menu

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.contextPopup

@Adaptive
fun <T> optContextMenu(
    viewBackend: MenuViewBackend<T>?,
    content: @Adaptive () -> Unit
): AdaptiveFragment {

    if (viewBackend == null) {
        content()
    } else {
        box {
            content()
            contextPopup(viewBackend) {
                menu(viewBackend)
            }
        }
    }

    return fragment()
}