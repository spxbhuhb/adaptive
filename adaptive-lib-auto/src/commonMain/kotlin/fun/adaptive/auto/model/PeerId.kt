package `fun`.adaptive.auto.model

import kotlin.jvm.JvmInline

typealias PeerId = Long

internal const val PEER_ID_ORIGIN = 0L
internal const val PEER_ID_CONNECTING = -1L

//@JvmInline
//value class PeerId(val value: Long) : Comparable<PeerId> {
//
//    override fun compareTo(other: PeerId): Int = value.compareTo(other.value)
//
//    override fun toString(): String {
//        return value.toString()
//    }
//
//    companion object {
//        val ORIGIN = PeerId(0L)
//        val CONNECTING = PeerId(-1L)
//    }
//
//}