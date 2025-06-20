package `fun`.adaptive.sandbox.recipe.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.button.dangerButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.modal.dialog
import `fun`.adaptive.ui.popup.modal.basicModal
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave

@Adaptive
fun openPopupOnClickExample(): AdaptiveFragment {

    dangerButton("Left-click to open popup") {
        dialog(adapter(), "data for the popup", ::popupContent)
    }

    return fragment()
}

@Adaptive
private fun popupContent(
    data: String,
    close: UiClose
) {
    localContext(close) {
        localContext(UiSave { close.uiClose() }) {
            basicModal("Popup content") {
                column {
                    padding { 16.dp }
                    text("This is the content of the popup: $data")
                }
            }
        }
    }
}