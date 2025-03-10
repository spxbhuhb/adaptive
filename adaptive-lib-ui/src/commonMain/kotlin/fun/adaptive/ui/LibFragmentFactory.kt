package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveFragmentFactory
import `fun`.adaptive.ui.codefence.codeFence
import `fun`.adaptive.ui.misc.todo
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.wsCenterPane
import `fun`.adaptive.ui.workspace.wsNoContent

object LibFragmentFactory : AdaptiveFragmentFactory() {
    init {
        add("lib:codefence", ::codeFence)
        add("lib:todo", ::todo)
        add(Workspace.WS_CENTER_PANE, ::wsCenterPane)
        add(Workspace.WS_NO_CONTENT, ::wsNoContent)
    }
}