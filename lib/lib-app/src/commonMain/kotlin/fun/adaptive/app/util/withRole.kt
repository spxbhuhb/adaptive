package `fun`.adaptive.app.util

import `fun`.adaptive.app.ClientApplication
import `fun`.adaptive.auth.model.Session
import `fun`.adaptive.log.getLogger
import `fun`.adaptive.runtime.AbstractApplication
import `fun`.adaptive.value.AvValueId

fun AbstractApplication<*>.withRole(role: AvValueId?, block : () -> Unit) {

    if (role == null) {
        getLogger("application").warning("using withRole with null role")
        return
    }

    val app = this as? ClientApplication<*>

    if (app == null) {
        getLogger("application").warning("using withRole, but the application is not a ClientApplication")
        return
    }

    val session = app.genericSessionOrNull as Session

    if (role in session.roles) {
        block()
    }
}