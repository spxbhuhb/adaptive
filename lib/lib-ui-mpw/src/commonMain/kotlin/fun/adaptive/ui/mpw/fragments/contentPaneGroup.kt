package `fun`.adaptive.ui.mpw.fragments

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.api.actualize
import `fun`.adaptive.foundation.instruction.emptyInstructions
import `fun`.adaptive.foundation.value.valueFrom
import `fun`.adaptive.ui.tab.tabContainer
import `fun`.adaptive.ui.tab.tabHandle
import `fun`.adaptive.ui.mpw.backends.ContentPaneGroupViewBackend

@Adaptive
fun contentPaneGroup(
    group: ContentPaneGroupViewBackend
) {
    if (group.isSingular) {
        singularGroup(group)
    } else {
        nonSingularGroup(group)
    }
}

@Adaptive
fun singularGroup(group: ContentPaneGroupViewBackend) {
    actualize(group.panes.first().key, emptyInstructions, group.panes.first())
}

@Adaptive
fun nonSingularGroup(group: ContentPaneGroupViewBackend) {
    val tabContainer = valueFrom { group.tabContainer }

    // https://github.com/spxbhuhb/adaptive/issues/123
    // KT-75416 KJS / IC: "IrLinkageError: Constructor can not be called: No constructor found for symbol" on jsBrowserDevelopmentRun restart
    tabContainer(tabContainer, {a,b,c,d -> tabHandle(a,b,c,d)})

}