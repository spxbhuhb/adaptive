package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.AutoGeneric
import `fun`.adaptive.auto.internal.origin.AutoInstance
import `fun`.adaptive.utility.UUID

/**
 * @property  globalId    The global ID of the data this group of auto backends handle.
 * @property  peerId      The id assigned to the connecting peer by an origin peer.
 */
@Adat
class AutoHandle(
    val globalId: UUID<AutoGeneric>,
    val peerId: PeerId,
    val itemId: ItemId?,
) {
    companion object {

        fun origin(collection: Boolean) =
            AutoHandle(
                UUID<AutoGeneric>(),
                PeerId.ORIGIN,
                if (collection) null else LamportTimestamp.ORIGIN
            )

    }
}