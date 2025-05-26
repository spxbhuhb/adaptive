package `fun`.adaptive.app.ws

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ws.admin.AppAdminWsModule
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.MultiPaneWorkspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.utility.firstInstance

val AdaptiveFragment.wsApplication
    get() = this.firstContext<ClientApplication<MultiPaneWorkspace>>()

@Deprecated("Use wsAddContent(NamedItem, Set<EventModifier>) instead")
fun AdaptiveFragment.wsAddContent(item: NamedItem, modifiers: Set<EventModifier> = emptySet()) {
    wsApplication.workspace.addContent(item.type, item, modifiers)
}

val AbstractApplication<*>.wsAppMain
    get() = modules.firstInstance<AppMainWsModule<*>>()

fun MultiPaneWorkspace.addAdminItem(item : SingularWsItem) {
    contexts.firstInstance<AppAdminWsModule<*>>().adminItems += item
}