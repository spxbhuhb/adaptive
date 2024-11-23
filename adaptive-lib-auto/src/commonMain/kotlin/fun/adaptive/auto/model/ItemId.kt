package `fun`.adaptive.auto.model

import kotlin.jvm.JvmInline

typealias ItemId = LamportTimestamp

internal val ITEM_ID_ORIGIN = LamportTimestamp.ORIGIN
internal val ITEM_ID_CONNECTING = LamportTimestamp.CONNECTING

//@JvmInline
//value class ItemId(val value: LamportTimestamp) : Comparable<ItemId> {
//
//    val timestamp
//        get() = value.timestamp
//
//    override fun compareTo(other: ItemId): Int = value.compareTo(other.value)
//
//    override fun toString(): String {
//        return value.toString()
//    }
//
//    companion object {
//        val ORIGIN = ItemId(LamportTimestamp.ORIGIN)
//        val CONNECTING = ItemId(LamportTimestamp.CONNECTING)
//    }
//
//}