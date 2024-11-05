package `fun`.adaptive.auto.model

value class ItemId(val value: LamportTimestamp) : Comparable<ItemId> {

    val timestamp
        get() = value.timestamp

    override fun compareTo(other: ItemId): Int = value.compareTo(other.value)

    companion object {
        val CONNECTING = ItemId(LamportTimestamp.CONNECTING)
    }

}