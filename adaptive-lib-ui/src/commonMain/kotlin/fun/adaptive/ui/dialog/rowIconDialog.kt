package `fun`.adaptive.ui.dialog

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.foundation.instructions
import `fun`.adaptive.resource.graphics.GraphicsResourceSet
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.onClose
import `fun`.adaptive.ui.icon.IconTheme
import `fun`.adaptive.ui.icon.actionIcon
import `fun`.adaptive.ui.icon.tableIconTheme

/**
 * An icon (24x24) that opens the dialog built by [modalContent] when clicked.
 *
 * Intended for table rows which hide/show icons on hover.
 *
 * Use the `close` parameter of [modalContent] to close the modal.
 *
 * @param  icon          The drawable resource of the icon
 * @param  title         Title of the dialog.
 * @param  modalContent  Content of the dialog.
 */
@Adaptive
fun rowIconDialog(
    icon: GraphicsResourceSet,
    title: String,
    vararg instructions: AdaptiveInstruction,
    feedback: ((Boolean) -> Unit)? = null,
    theme: IconTheme = tableIconTheme,
    @Adaptive _fixme_adaptive_content: (close: () -> Unit) -> Unit
): AdaptiveFragment {
    var modalOpen = false

    actionIcon(icon, instructions(), theme = theme) .. onClick { modalOpen = true; feedback?.invoke(true) }

    if (modalOpen) {
        dialog(title) {
            onClose { modalOpen = false; feedback?.invoke(false) }
            _fixme_adaptive_content { modalOpen = false; feedback?.invoke(false) }
        }
    }

    return fragment()
}