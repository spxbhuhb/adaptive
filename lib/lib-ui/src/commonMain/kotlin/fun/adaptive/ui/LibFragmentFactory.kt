package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.popup.feedbackPopupContent
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.wsCenterPane
import `fun`.adaptive.ui.workspace.wsEmptyCenterPane

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:feedback-popup-content", ::feedbackPopupContent)
        add("lib:codefence", ::codeFence)
        add("lib:todo", ::todo)
        add(MultiPaneWorkspace.WS_CENTER_PANE, ::wsCenterPane)
        add(MultiPaneWorkspace.WSPANE_EMPTY, ::wsEmptyCenterPane)
    }
}