package `fun`.adaptive.value.operation

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.value.AvValue
import kotlinx.coroutines.channels.Channel
import kotlin.reflect.KClass

sealed class AvValueOperation : AdatClass {

    var channel: Channel<Any?>? = null

    fun fail(message: String) = channel?.trySend(RuntimeException(message))

    fun fail(ex: Exception?) = channel?.trySend(ex ?: RuntimeException())

    fun success(value: Any? = null) = channel?.trySend(value)

    fun forEach(block: (operation: AvValueOperation) -> Unit) {
        if (this is AvoTransaction) {
            operations.forEach(block)
        } else {
            block(this)
        }
    }

    suspend fun forEachSuspend(block: suspend (operation: AvValueOperation) -> Unit) {
        if (this is AvoTransaction) {
            for (operation in operations) {
                operation.forEachSuspend(block)
            }
        } else {
            block(this)
        }
    }

    suspend fun <T : Any> forEachValueSuspend(
        specClass: KClass<T>,
        strict: Boolean = true,
        blockFun: suspend (value : AvValue<T>) -> Unit
    ) {

        suspend fun process(operation: AvValueOperation) {
            when (operation) {
                is AvoAdd -> operation.value
                is AvoUpdate -> operation.value
                is AvoAddOrUpdate -> operation.value
                else -> return
            }.also {
                if (specClass.isInstance(it.spec)) {
                    @Suppress("UNCHECKED_CAST")
                    blockFun(it as AvValue<T>)
                } else {
                    if (strict) {
                        fail("invalid spec type: ${it.spec::class} != $specClass in $it")
                    }
                }
            }
        }

        forEachSuspend(::process)
    }

    suspend fun <T : Any> forEachSpecSuspend(
        specClass: KClass<T>,
        strict: Boolean = true,
        blockFun: suspend (spec: T) -> Unit
    ) {

        suspend fun process(operation: AvValueOperation) {
            when (operation) {
                is AvoAdd -> operation.value
                is AvoUpdate -> operation.value
                is AvoAddOrUpdate -> operation.value
                else -> return
            }.also {
                if (specClass.isInstance(it.spec)) {
                    @Suppress("UNCHECKED_CAST")
                    blockFun(it.spec as T)
                } else {
                    if (strict) {
                        fail("invalid spec type: ${it.spec::class} != $specClass in $it")
                    }
                }
            }
        }

        forEachSuspend(::process)

    }

}