package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.popup.feedbackPopupContent
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.wsCenterPane
import `fun`.adaptive.ui.workspace.wsEmptyCenterPane

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:feedback-popup-content", ::feedbackPopupContent)
        add("lib:codefence", ::codeFence)
        add("lib:todo", ::todo)
        add(Workspace.WS_CENTER_PANE, ::wsCenterPane)
        add(Workspace.WSPANE_EMPTY, ::wsEmptyCenterPane)
    }
}