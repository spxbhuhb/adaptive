package hu.simplexion.adaptive.crdt.api

import hu.simplexion.adaptive.crdt.FragmentStore
import hu.simplexion.adaptive.crdt.LamportTimestamp
import hu.simplexion.adaptive.service.ServiceApi
import hu.simplexion.adaptive.utility.UUID

@ServiceApi
interface FragmentStoreApi {
    suspend fun connect(globalStoreId: UUID<FragmentStore>): LamportTimestamp
    suspend fun disconnect(globalStoreId: UUID<FragmentStore>)
}