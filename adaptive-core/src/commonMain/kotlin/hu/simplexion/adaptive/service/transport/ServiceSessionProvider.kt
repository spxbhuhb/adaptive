package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.utility.UUID

interface ServiceSessionProvider<ST> {
    fun getKey(): String
    fun getSession(uuid: UUID<*>): ST
}