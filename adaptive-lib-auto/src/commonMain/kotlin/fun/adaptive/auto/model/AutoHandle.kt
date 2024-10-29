package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.internal.backend.AutoBackend
import `fun`.adaptive.utility.UUID

/**
 * @property  globalId    The global ID of the data this group of auto backends handle.
 * @property  peerId      The id assigned to the connecting peer by an origin peer.
 */
@Adat
class AutoHandle(
    val globalId: UUID<AutoBackend>,
    val peerId: PeerId,
    val itemId: ItemId?,
) {
    constructor() : this(UUID(), 0, null)
}