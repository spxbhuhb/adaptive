package `fun`.adaptive.service.model

import `fun`.adaptive.utility.CleanupHandler
import `fun`.adaptive.utility.UUID

interface ServiceSession {

    val uuid: UUID<*>

    val principalOrNull: UUID<*>?

    fun addSessionCleanup(cleanup: CleanupHandler<ServiceSession>)

    fun removeSessionCleanup(cleanup: CleanupHandler<ServiceSession>)

    fun cleanup()

}