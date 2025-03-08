package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.workspace.model.Workspace
import `fun`.adaptive.ui.workspace.wsCenterPane

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:codefence", ::codeFence)
        add("lib:todo", ::todo)
        add(Workspace.WS_CENTER_PANE, ::wsCenterPane)
    }
}