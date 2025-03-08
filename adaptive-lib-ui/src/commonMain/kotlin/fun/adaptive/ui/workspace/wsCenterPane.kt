package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.workspace.model.Workspace

@Adaptive
fun wsCenterPane() : AdaptiveFragment {
    val workspace = fragment().firstContext<Workspace>()

    wsContentPaneGroup(workspace.contentPaneGroups.first())

    return fragment()
}