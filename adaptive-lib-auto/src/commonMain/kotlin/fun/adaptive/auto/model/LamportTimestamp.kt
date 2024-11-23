package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.AdatCompanion
import kotlin.math.max

@Adat
class LamportTimestamp(
    val peerId: PeerId,
    val timestamp: LamportTime,
) : Comparable<LamportTimestamp> {

    override fun compareTo(other: LamportTimestamp): Int =
        when {
            this.timestamp < other.timestamp -> - 1
            this.timestamp > other.timestamp -> 1
            // Not that here order is reversed, lower peerId is considered greater
            // This means that the peer that joined earlier is considered stronger.
            this.peerId > other.peerId -> - 1
            this.peerId < other.peerId -> 1
            else -> 0
        }

    fun receive(remote: LamportTimestamp): LamportTimestamp =
        LamportTimestamp(peerId, max(remote.timestamp, timestamp))

    fun increment(): LamportTimestamp =
        LamportTimestamp(peerId, timestamp + 1)

    override fun toString(): String {
        return "$peerId:$timestamp"
    }

    companion object : AdatCompanion<LamportTimestamp> {
        /**
         * Timestamp used by connecting peers.
         */
        val CONNECTING = LamportTimestamp(PEER_ID_CONNECTING, 0)

        /**
         * Timestamp used by origins, the first instances of an auto structure.
         */
        val ORIGIN = LamportTimestamp(PEER_ID_ORIGIN, 1)
    }
}