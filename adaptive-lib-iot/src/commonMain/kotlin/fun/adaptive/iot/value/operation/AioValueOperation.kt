package `fun`.adaptive.iot.value.operation

import `fun`.adaptive.adat.AdatClass
import kotlinx.coroutines.channels.Channel

sealed class AioValueOperation : AdatClass {

    val channel : Channel<Boolean>? = null

    fun fail() = channel?.trySend(false)

    fun success() = channel?.trySend(true)

    fun forEach(block: (operation: AioValueOperation) -> Unit) {
        if (this is AvoTransaction) {
            operations.forEach(block)
        } else {
            block(this)
        }
    }
}