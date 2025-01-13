package `fun`.adaptive.ui.dialog.api

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.api.text

/**
 * A label that opens the dialog built by [modalContent] when clicked.
 *
 * Use the `close` parameter of [modalContent] to close the modal.
 *
 * @param  label         Content of the label.
 * @param  title         Title of the dialog.
 * @param  modalContent  Content of the dialog.
 */
@Adaptive
fun textDialog(
    label: String,
    title: String,
    vararg instructions: AdaptiveInstruction,
    @Adaptive _fixme_adaptive_content: (close: () -> Unit) -> Unit,
): AdaptiveFragment {
    var modalOpen = false

    text(label, instructions()) .. onClick { modalOpen = true }

    if (modalOpen) {
        dialog(title) {
            onClose { modalOpen = false }
            _fixme_adaptive_content { modalOpen = false }
        }
    }

    return fragment()
}