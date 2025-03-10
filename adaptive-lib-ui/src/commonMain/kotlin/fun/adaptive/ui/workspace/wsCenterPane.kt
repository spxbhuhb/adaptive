package `fun`.adaptive.ui.workspace

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.valueFrom

@Adaptive
fun wsCenterPane(workspace: Workspace): AdaptiveFragment {

    val groups = valueFrom { workspace.contentPaneGroups }

    wsContentPaneGroup(groups.first())

    return fragment()
}