package `fun`.adaptive.doc.example.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.button.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.modal.dialog
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.theme.textColors


/**
 * # Custom modal popup
 *
 * Dialog accepts any kind of fragment, it does not have to be a standard popup.
 */
@Adaptive
fun modalPopupCustom(): AdaptiveFragment {

    button("Click for custom popup") {
        dialog(adapter(), "data for the popup", ::customPopupContent)
    }

    return fragment()
}

@Adaptive
private fun customPopupContent(
    data: String,
    close: UiClose
) {
    column {
        backgrounds.warningSurface .. padding { 16.dp }
        onClick { close.uiClose() }

        text("This popup does not use the standard styling.") .. textColors.onWarningSurface
        text("Click inside the popup area to hide.") .. textColors.onWarningSurface
    }

}