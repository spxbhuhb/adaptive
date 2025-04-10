package `fun`.adaptive.ui.dialog

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.button.ButtonTheme
import `fun`.adaptive.ui.button.button
import `fun`.adaptive.ui.button.dangerButton

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
    resource: GraphicsResourceSet,
    title: String,
    vararg instructions: AdaptiveInstruction,
    @Adaptive _fixme_adaptive_content: (close: () -> Unit) -> Unit
): AdaptiveFragment {
    var modalOpen = false

    button(label, resource, ButtonTheme.DEFAULT, instructions()) .. onClick { modalOpen = true }

    if (modalOpen) {
        dialog(title) {
            onClose { modalOpen = false }
            _fixme_adaptive_content { modalOpen = false }
        }
    }

    return fragment()
}

/**
 * An icon button that opens the dialog built by [modalContent] when clicked.
 *
 * Use the `close` parameter of [modalContent] to close the modal.
 *
 * @param  label         The label of the button.
 * @param  icon          The resource of the button icon.
 * @param  title         Title of the dialog.
 * @param  modalContent  Content of the dialog.
 */
@Adaptive
fun dangerButtonDialog(
    label: String,
    icon: GraphicsResourceSet,
    title: String,
    vararg instructions: AdaptiveInstruction,
    @Adaptive _fixme_adaptive_content: (close: () -> Unit) -> Unit
): AdaptiveFragment {
    var modalOpen = false

    dangerButton(label, icon, ButtonTheme.DEFAULT, instructions()) .. onClick { modalOpen = true }

    if (modalOpen) {
        dialog(title) {
            onClose { modalOpen = false }
            _fixme_adaptive_content { modalOpen = false }
        }
    }

    return fragment()
}