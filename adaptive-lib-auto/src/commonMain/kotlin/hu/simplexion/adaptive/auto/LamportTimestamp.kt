package hu.simplexion.adaptive.auto

import hu.simplexion.adaptive.adat.Adat
import kotlin.math.max

@Adat
class LamportTimestamp(
    val backendId: BackendId,
    val timestamp: Int,
) : Comparable<LamportTimestamp> {

    override fun compareTo(other: LamportTimestamp): Int =
        when {
            this.timestamp < other.timestamp -> - 1
            this.timestamp > other.timestamp -> 1
            this.backendId < other.backendId -> - 1
            this.backendId > other.backendId -> 1
            else -> 0
        }

    fun receive(remote: LamportTimestamp): LamportTimestamp =
        LamportTimestamp(backendId, max(remote.timestamp, timestamp))

    fun increment(): LamportTimestamp =
        LamportTimestamp(backendId, timestamp + 1)

    override fun toString(): String {
        return "$backendId:$timestamp"
    }

}