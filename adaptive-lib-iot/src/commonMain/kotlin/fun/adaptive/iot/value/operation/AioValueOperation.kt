package `fun`.adaptive.iot.value.operation

import `fun`.adaptive.adat.AdatClass
import kotlinx.coroutines.channels.Channel

sealed class AioValueOperation : AdatClass {

    var channel : Channel<Any>? = null

    fun fail(message : String) = channel?.trySend(RuntimeException(message))

    fun fail(ex : Exception?) = channel?.trySend(ex ?: RuntimeException())

    fun success(value : Any? = null) = channel?.trySend(value ?: Unit)

    fun forEach(block: (operation: AioValueOperation) -> Unit) {
        if (this is AvoTransaction) {
            operations.forEach(block)
        } else {
            block(this)
        }
    }
}