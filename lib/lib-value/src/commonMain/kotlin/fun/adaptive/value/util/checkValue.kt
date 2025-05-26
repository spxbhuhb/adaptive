@file:Suppress("UNCHECKED_CAST")

package `fun`.adaptive.value.util

import `fun`.adaptive.value.AvValue
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
inline fun checkValue(value : Any?) : AvValue<*> {
    contract {
        returns() implies (value is AvValue<*>)
    }
    check(value is AvValue<*>)
    return value
}