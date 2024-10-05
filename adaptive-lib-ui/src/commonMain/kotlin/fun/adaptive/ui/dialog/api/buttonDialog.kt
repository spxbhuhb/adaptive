package `fun`.adaptive.ui.dialog.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.resource.DrawableResource
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.button.api.button

/**
 * An icon button that opens the dialog built by [modalContent] when clicked.
 *
 * Use the `close` parameter of [modalContent] to close the modal.
 *
 * @param  label         The label of the button.
 * @param  resource      The drawable resource of the button icon.
 * @param  title         Title of the dialog.
 * @param  modalContent  Content of the dialog.
 */
@Adaptive
fun buttonDialog(
    label: String,
    resource: DrawableResource,
    title: String,
    vararg instructions: AdaptiveInstruction,
    @Adaptive modalContent: (close: () -> Unit) -> Unit
): AdaptiveFragment {
    var modalOpen = false

    button(label, resource, *instructions) .. onClick { modalOpen = true }

    if (modalOpen) {
        dialog(title) {
            onClose { modalOpen = false }
            modalContent { modalOpen = false }
        }
    }

    return fragment()
}
