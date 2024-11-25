package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import `fun`.adaptive.auto.api.AutoGeneric
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
    companion object : AdatCompanion<AutoHandle> {

        fun origin(collection: Boolean) =
            AutoHandle(
                UUID<AutoGeneric>(),
                PEER_ID_ORIGIN,
                if (collection) null else ITEM_ID_ORIGIN
            )
    }
}