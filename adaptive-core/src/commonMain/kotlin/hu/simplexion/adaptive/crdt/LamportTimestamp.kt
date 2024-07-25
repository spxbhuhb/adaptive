package hu.simplexion.adaptive.crdt

import kotlin.math.max

class LamportTimestamp(
    val timestamp: Int,
    val storeId: StoreId
) : Comparable<LamportTimestamp> {

    override fun compareTo(other: LamportTimestamp): Int =
        when {
            this.timestamp < other.timestamp -> - 1
            this.timestamp > other.timestamp -> 1
            this.storeId < other.storeId -> - 1
            this.storeId > other.storeId -> 1
            else -> 0
        }

    fun receive(remote: LamportTimestamp): LamportTimestamp =
        LamportTimestamp(max(remote.timestamp, timestamp) + 1, storeId)

    fun increment(): LamportTimestamp =
        LamportTimestamp(timestamp + 1, storeId)

}