package `fun`.adaptive.runtime

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.api.firstContext
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.utility.UUID

val AdaptiveFragment.application
    get() = this.firstContext<AbstractClientApplication<FrontendWorkspace, BackendWorkspace>>()

fun AdaptiveFragment.hasRole(name: String) : Boolean {
    return application.hasRole(name)
}

fun AdaptiveFragment.hasRole(uuid: UUID<*>?) : Boolean {
    if (uuid == null) {
        return false
    }

    return application.hasRole(uuid)
}