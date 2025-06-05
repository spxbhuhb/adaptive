package `fun`.adaptive.app.ui.common

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace

val AdaptiveFragment.application
    get() = this.firstContext<ClientApplication<FrontendWorkspace, BackendWorkspace>>()