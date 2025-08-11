package `fun`.adaptive.app.ui.util

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.app.ui.common.application
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.value.AvValueId

fun AdaptiveFragment.hasRole(name: String) : Boolean {
    return application.hasRole(name)
}


fun AdaptiveFragment.hasRole(uuid: AvValueId?) : Boolean {
    return application.hasRole(uuid)
}

fun AbstractApplication<*,*>.withRole(role: AvValueId?, block : () -> Unit) {
    if (hasRole(role)) {
        block()
    }
}

fun AbstractApplication<*,*>.hasRole(name: String): Boolean {
    val app = this as? ClientApplication<*,*>
    if (app == null) {
        getLogger("application").warning("using hasRole, but the application is not a ClientApplication")
        return false
    }

    return app.allApplicationRoles.firstOrNull { it.name == name }?.let {
        val session = app.genericSessionOrNull as Session
        return it.uuid in session.roles
    } ?: false
}

fun AbstractApplication<*,*>.hasRole(uuid: AvValueId?): Boolean {
    if (uuid == null) {
        getLogger("application").warning("using withRole with null role")
        return false
    }

    val app = this as? ClientApplication<*,*>

    if (app == null) {
        getLogger("application").warning("using withRole, but the application is not a ClientApplication")
        return false
    }

    val session = app.genericSessionOrNull as Session

    return uuid in session.roles
}