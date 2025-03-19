package `fun`.adaptive.iot.value.operation

import kotlinx.coroutines.channels.Channel

sealed class AioValueOperation {

    val channel : Channel<Boolean>? = null

    fun fail() = channel?.trySend(false)

    fun success() = channel?.trySend(true)
    
}