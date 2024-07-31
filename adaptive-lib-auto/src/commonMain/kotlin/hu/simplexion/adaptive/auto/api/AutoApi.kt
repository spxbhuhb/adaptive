package hu.simplexion.adaptive.auto.api

import hu.simplexion.adaptive.auto.FragmentStore
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.utility.UUID

@ServiceApi
interface AutoApi {
    suspend fun connect(globalId: UUID<AutoBackend>): LamportTimestamp
    suspend fun disconnect(globalId: UUID<FragmentStore>)
}