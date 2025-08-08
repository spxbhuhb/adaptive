package `fun`.adaptive.doc.example.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.input.button.submitButton
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.modal.basicModal
import `fun`.adaptive.ui.popup.modal.dialog
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave

/**
 * # Standard popup
 *
 * - use [dialog](function://) to show a modal popup, the popup is **independent** of the current fragment
 * - use [basicModal](fragment://) to show a basic modal with standard decorations
 */
@Adaptive
fun modalPopupStandard(): AdaptiveFragment {

    submitButton("Click for basic popup") {
        dialog(adapter(), "data for the popup", ::basicPopupContent)
    }

    return fragment()
}

@Adaptive
private fun basicPopupContent(
    data: String,
    close: UiClose
) {
    localContext(close) {
        localContext(UiSave { close.uiClose() }) {
            basicModal("Popup title") {
                column {
                    padding { 16.dp }
                    text("This is the content of the popup: $data")
                }
            }
        }
    }
}