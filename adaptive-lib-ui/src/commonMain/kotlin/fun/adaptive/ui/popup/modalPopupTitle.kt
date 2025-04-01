package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.builtin.close

@Adaptive
fun modalPopupTitle(
    title: String,
    theme: PopupTheme,
    hide: () -> Unit,
): AdaptiveFragment {

    grid(instructions()) {
        theme.modalTitleContainer
        svg(Graphics.close) .. theme.modalTitleIcon .. onClick { hide() }
        text(title) .. theme.modalTitleText
    }

    return fragment()
}