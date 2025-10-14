package `fun`.adaptive.service.model

import `fun`.adaptive.service.ServiceSessionId
import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.UUID

interface ServiceSession {

    val uuid: ServiceSessionId

    val principalOrNull: UUID<*>?

    val roles: Set<UUID<*>>

    fun addSessionCleanup(cleanup: CleanupHandler<ServiceSession>)

    fun removeSessionCleanup(cleanup: CleanupHandler<ServiceSession>)

    fun cleanup()

}