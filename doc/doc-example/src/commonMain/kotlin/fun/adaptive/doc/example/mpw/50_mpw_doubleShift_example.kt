package `fun`.adaptive.doc.example.mpw

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.adapter
import `fun`.adaptive.foundation.api.localContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.runtime.NoBackendWorkspace
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.fragments.multiPaneWorkspace
import `fun`.adaptive.ui.popup.modal.basicModal
import `fun`.adaptive.ui.popup.modal.dialog
import `fun`.adaptive.ui.support.UiClose
import `fun`.adaptive.ui.support.UiSave
import `fun`.adaptive.ui.theme.backgrounds

/**
 * # Double shift
 *
 * Set the [doubleShiftHandler](property://MultiPaneWorkspace) property of the
 * workspace to execute a function on double shift.
 *
 * Double shift means the user presses the Shift key twice in 0.3 seconds.
 *
 * (This example uses the components from the `All Panes` example.)
 *
 * **Click into the example workspace to make double-shift local to it.**
 */
@Adaptive
fun mpwDoubleShiftExample(): AdaptiveFragment {

    val workspace = MultiPaneWorkspace(
        adapter().backend,
        NoBackendWorkspace(),
        toolSizeDefault = 100.dp
    ).also {
        it.doubleShiftHandler = {
            dialog(adapter(), "dialog data", ::doubleShiftDialog)
        }

        initPanes(it)
    }

    box {
        maxWidth .. height { 600.dp } .. backgrounds.friendlyOpaque .. padding { 16.dp }
        multiPaneWorkspace(workspace) .. maxSize .. backgrounds.surface
    }

    return fragment()
}

@Adaptive
fun doubleShiftDialog(
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