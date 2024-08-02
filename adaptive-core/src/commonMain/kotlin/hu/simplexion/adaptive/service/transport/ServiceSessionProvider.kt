package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.model.ServiceSession
import hu.simplexion.adaptive.utility.UUID

interface ServiceSessionProvider {
    fun getKey(): String
    fun getSession(uuid: UUID<*>): ServiceSession?
}