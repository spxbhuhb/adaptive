package `fun`.adaptive.auto.model

import kotlin.jvm.JvmInline

@JvmInline
value class ItemId(val value: LamportTimestamp) : Comparable<ItemId> {

    val timestamp
        get() = value.timestamp

    override fun compareTo(other: ItemId): Int = value.compareTo(other.value)

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        val ORIGIN = ItemId(LamportTimestamp.ORIGIN)
        val CONNECTING = ItemId(LamportTimestamp.CONNECTING)
    }

}