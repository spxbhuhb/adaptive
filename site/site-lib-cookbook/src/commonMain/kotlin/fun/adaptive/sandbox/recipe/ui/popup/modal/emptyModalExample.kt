package `fun`.adaptive.sandbox.recipe.ui.popup.modal

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.storeFor
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.api.width
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.popup.modal.modalPopup
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave

@Adaptive
fun emptyModalExample(): AdaptiveFragment {

    val messageStore = storeFor { emptyList<String>() }
    val messages = valueFrom { messageStore }

    localContext(UiClose { messageStore.value += "close" }) {
        localContext(UiSave { messageStore.value += "save" }) {

            modalPopup("Hello World!") {
                width { 600.dp } .. height { 400.dp }

                column {
                    maxSize .. verticalScroll .. padding { 16.dp }
                    for (message in messages) text(message)
                }
            }
        }

    }

    return fragment()
}