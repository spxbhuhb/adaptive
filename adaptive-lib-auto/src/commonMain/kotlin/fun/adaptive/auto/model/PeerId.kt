package `fun`.adaptive.auto.model

value class PeerId(val value: Long) : Comparable<PeerId> {

    override fun compareTo(other: PeerId): Int = value.compareTo(other.value)

    companion object {
        val ORIGIN = PeerId(0L)
        val CONNECTING = PeerId(-1L)
    }

}