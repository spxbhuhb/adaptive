package `fun`.adaptive.auto.model

value class ItemId(private val id: LamportTimestamp) : Comparable<ItemId> {


    fun asLamportTimestamp(): LamportTimestamp = id

    val timestamp
        get() = id.timestamp

    override fun compareTo(other: ItemId): Int = id.compareTo(other.id)

    companion object {
        val CONNECTING = ItemId(LamportTimestamp.CONNECTING)
    }

}