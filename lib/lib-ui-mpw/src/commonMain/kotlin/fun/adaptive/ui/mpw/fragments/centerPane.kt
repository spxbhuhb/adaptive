package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.value.observe
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace

@Adaptive
fun centerPane(workspace: MultiPaneWorkspace): AdaptiveFragment {

    val groups = observe { workspace.contentPaneGroups }

    contentPaneGroup(groups.first())

    return fragment()
}