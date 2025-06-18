package `fun`.adaptive.ui.popup.modal.components

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.generated.resources.close
import `fun`.adaptive.ui.popup.PopupTheme
import `fun`.adaptive.ui.support.UiClose

@Adaptive
fun modalPopupTitle(
    title: String,
    close: UiClose?,
    theme: PopupTheme,
): AdaptiveFragment {

    box(instructions()) {
        theme.modalTitleContainer
        svg(Graphics.close) .. theme.modalTitleIcon .. onClick { close?.uiClose() ?: fragment().firstContext<UiClose>().uiClose() }
        text(title) .. theme.modalTitleText
    }

    return fragment()
}