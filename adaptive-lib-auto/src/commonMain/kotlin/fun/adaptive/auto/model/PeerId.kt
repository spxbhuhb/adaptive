package `fun`.adaptive.auto.model

value class PeerId(private val id: Long) : Comparable<PeerId> {

    override fun compareTo(other: PeerId): Int = id.compareTo(other.id)

    companion object {
        val ORIGIN = PeerId(0L)
        val CONNECTING = PeerId(-1L)
    }

}