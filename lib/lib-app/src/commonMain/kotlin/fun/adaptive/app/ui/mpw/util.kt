package `fun`.adaptive.app.ui.mpw

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.app.AppMainModuleMpw
import `fun`.adaptive.app.ui.mpw.admin.AppAdminModuleMpw
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.model.NamedItem
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.ui.instruction.event.EventModifier
import `fun`.adaptive.ui.mpw.MultiPaneWorkspace
import `fun`.adaptive.ui.mpw.model.SingularPaneItem
import `fun`.adaptive.utility.firstInstance

val AdaptiveFragment.mpwApplication
    get() = this.firstContext<ClientApplication<MultiPaneWorkspace, BackendWorkspace>>()

fun AdaptiveFragment.addContent(item: NamedItem, modifiers: Set<EventModifier> = emptySet()) {
    mpwApplication.frontendWorkspace.addContent(item.type, item, modifiers)
}

val AbstractApplication<*,*>.mpwAppMainModule
    get() = modules.firstInstance<AppMainModuleMpw<*, *>>()

fun MultiPaneWorkspace.addAdminPlugin(item : SingularPaneItem) {
    contexts.firstInstance<AppAdminModuleMpw<*, *>>().plugins += item
}