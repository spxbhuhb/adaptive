package hu.simplexion.adaptive.auto.model

import hu.simplexion.adaptive.adat.Adat
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.auto.backend.AutoBackend
import hu.simplexion.adaptive.utility.UUID

/**
 * @property  globalId    The global ID of the data the auto backend handles.
 * @property  clientId    The id assigned to the connecting peer by an origin peer.
 * @property  originTime  The current time of the origin peer.
 */
@Adat
class AutoHandle(
    val globalId: UUID<AutoBackend>,
    val clientId: Int,
    val originTime: LamportTimestamp
)