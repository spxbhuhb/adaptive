package `fun`.adaptive.app.ws

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ws.admin.AppAdminWsModule
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.workspace.Workspace
import `fun`.adaptive.ui.workspace.model.SingularWsItem
import `fun`.adaptive.utility.firstInstance

val AdaptiveFragment.wsApplication
    get() = this.firstContext<ClientApplication<Workspace>>()

fun AdaptiveFragment.wsAddContent(item: NamedItem, modifiers: Set<EventModifier> = emptySet()) {
    wsApplication.workspace.addContent(item, modifiers)
}

val AbstractApplication<*>.wsAppMain
    get() = modules.firstInstance<AppMainWsModule<*>>()

fun Workspace.addAdminItem(item : SingularWsItem) {
    contexts.firstInstance<AppAdminWsModule<*>>().adminItems += item
}