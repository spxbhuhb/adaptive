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
        LamportTimestamp(max(remote.timestamp, timestamp) + 1, backendId)

    fun increment(): LamportTimestamp =
        LamportTimestamp(timestamp + 1, backendId)

}