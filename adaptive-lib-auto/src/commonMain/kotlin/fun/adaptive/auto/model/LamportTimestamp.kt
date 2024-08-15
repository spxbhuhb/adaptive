package `fun`.adaptive.auto.model

import `fun`.adaptive.adat.Adat
import kotlin.math.max

@Adat
class LamportTimestamp(
    val clientId: ClientId,
    val timestamp: Int,
) : Comparable<LamportTimestamp> {

    override fun compareTo(other: LamportTimestamp): Int =
        when {
            this.timestamp < other.timestamp -> - 1
            this.timestamp > other.timestamp -> 1
            this.clientId < other.clientId -> - 1
            this.clientId > other.clientId -> 1
            else -> 0
        }

    fun receive(remote: LamportTimestamp): LamportTimestamp =
        LamportTimestamp(clientId, max(remote.timestamp, timestamp))

    fun increment(): LamportTimestamp =
        LamportTimestamp(clientId, timestamp + 1)

    override fun toString(): String {
        return "$clientId:$timestamp"
    }

}