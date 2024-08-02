package hu.simplexion.adaptive.service.model

import hu.simplexion.adaptive.utility.CleanupHandler
import hu.simplexion.adaptive.utility.UUID

interface ServiceSession<PT> {

    val principalOrNull: UUID<PT>?

    fun addSessionCleanup(cleanup: CleanupHandler<ServiceSession<PT>>)

    fun removeSessionCleanup(cleanup: CleanupHandler<ServiceSession<PT>>)

    fun cleanup()

}