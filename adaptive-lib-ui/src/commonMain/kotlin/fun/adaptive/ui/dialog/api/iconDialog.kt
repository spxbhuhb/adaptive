package `fun`.adaptive.ui.dialog.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.icon.icon

/**
 * An icon (24x24) that opens the dialog built by [modalContent] when clicked.
 *
 * Use the `close` parameter of [modalContent] to close the modal.
 *
 * @param  resource      The drawable resource of the icon
 * @param  title         Title of the dialog.
 * @param  modalContent  Content of the dialog.
 */
@Adaptive
fun iconDialog(
    resource: DrawableResource,
    title: String,
    @Adaptive modalContent: (close: () -> Unit) -> Unit
) {
    var modalOpen = false

    icon(resource) .. onClick { modalOpen = true }

    if (modalOpen) {
        dialog(title) {
            onClose { modalOpen = false }
            modalContent { modalOpen = false }
        }
    }
}