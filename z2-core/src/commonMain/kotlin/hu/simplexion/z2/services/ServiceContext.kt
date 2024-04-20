package hu.simplexion.z2.services

import hu.simplexion.z2.util.UUID

interface ServiceContext {
    val uuid: UUID<ServiceContext>
    var data: MutableMap<Any, Any?>
}