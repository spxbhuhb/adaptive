package hu.simplexion.adaptive.service.transport

import hu.simplexion.adaptive.service.ServiceContext
import hu.simplexion.adaptive.service.model.ServiceSession
import hu.simplexion.adaptive.utility.UUID

interface ServiceSessionProvider {
    fun getSession(uuid: UUID<ServiceContext>): ServiceSession?
}