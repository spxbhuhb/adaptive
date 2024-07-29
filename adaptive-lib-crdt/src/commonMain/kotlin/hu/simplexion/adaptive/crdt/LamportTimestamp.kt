package hu.simplexion.adaptive.crdt

import hu.simplexion.adaptive.adat.Adat
import kotlin.math.max

@Adat
class LamportTimestamp(
    val timestamp: Int,
    val instanceId: StoreId
) : Comparable<LamportTimestamp> {

    override fun compareTo(other: LamportTimestamp): Int =
        when {
            this.timestamp < other.timestamp -> - 1
            this.timestamp > other.timestamp -> 1
            this.instanceId < other.instanceId -> - 1
            this.instanceId > other.instanceId -> 1
            else -> 0
        }

    fun receive(remote: LamportTimestamp): LamportTimestamp =
        LamportTimestamp(max(remote.timestamp, timestamp) + 1, instanceId)

    fun increment(): LamportTimestamp =
        LamportTimestamp(timestamp + 1, instanceId)

}