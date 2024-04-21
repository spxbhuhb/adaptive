package hu.simplexion.z2.services

import hu.simplexion.z2.utility.UUID

data class BasicServiceContext(
    override val uuid: UUID<ServiceContext> = UUID(),
    override var data: MutableMap<Any, Any?> = mutableMapOf()
) : ServiceContext