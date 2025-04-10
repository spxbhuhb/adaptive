package `fun`.adaptive.service.transport

import `fun`.adaptive.service.ServiceContext
import `fun`.adaptive.service.model.ServiceSession
import `fun`.adaptive.utility.UUID

interface ServiceSessionProvider {
    fun getSession(uuid: UUID<ServiceContext>): ServiceSession?
}