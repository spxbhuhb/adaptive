package hu.simplexion.adaptive.service.model

import hu.simplexion.adaptive.utility.CleanupHandler
import hu.simplexion.adaptive.utility.UUID

interface ServiceSession {

    val id: UUID<*>

    val principalOrNull: UUID<*>?

    fun addSessionCleanup(cleanup: CleanupHandler<ServiceSession>)

    fun removeSessionCleanup(cleanup: CleanupHandler<ServiceSession>)

    fun cleanup()

}