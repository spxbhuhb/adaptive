package `fun`.adaptive.auto.model

import kotlin.jvm.JvmInline

@JvmInline
value class PeerId(val value: Long) : Comparable<PeerId> {

    override fun compareTo(other: PeerId): Int = value.compareTo(other.value)

    override fun toString(): String {
        if (value == 0L) return "ORIGIN"
        if (value == -1L) return "CONNECTING"
        return value.toString()
    }

    companion object {
        val ORIGIN = PeerId(0L)
        val CONNECTING = PeerId(-1L)
    }

}