package hu.simplexion.adaptive.auto.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.utility.UUID

/**
 * @property  globalId    The global ID of the data this group of auto backends handle.
 * @property  clientId    The id assigned to the connecting peer by an origin peer.
 */
@Adat
class AutoHandle(
    val globalId: UUID<AutoBackend>,
    val clientId: Int
)