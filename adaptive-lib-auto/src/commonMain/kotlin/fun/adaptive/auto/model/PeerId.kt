package `fun`.adaptive.auto.model

value class PeerId(private val id: Long) {

    /**
     * Peer ID of the origin auto instance.
     */
    companion object {
        val ORIGIN = PeerId(0L)
    }

}