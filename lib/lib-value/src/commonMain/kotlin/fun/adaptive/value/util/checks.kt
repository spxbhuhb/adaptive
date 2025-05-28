@file:Suppress("UNCHECKED_CAST")

package `fun`.adaptive.value.util

import `fun`.adaptive.value.AvMarker
import `fun`.adaptive.value.AvValue
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
inline fun checkValue(value: Any?): AvValue<*> {
    contract {
        returns() implies (value is AvValue<*>)
    }
    check(value is AvValue<*>)
    return value
}

inline fun <reified SPEC : Any> isValue(instance: Any, marker: AvMarker? = null): Boolean =
    asValueOrNull(instance, SPEC::class, marker) != null

@OptIn(ExperimentalContracts::class)
inline fun <reified SPEC : Any> asValue(instance: Any, marker: AvMarker? = null): AvValue<SPEC> {
    contract {
        returns() implies (instance is AvValue<*>)
    }
    return asValue(instance, SPEC::class, marker)
}

fun <SPEC : Any> asValue(instance: Any, specClass: KClass<SPEC>, marker: AvMarker? = null): AvValue<SPEC> =
    checkNotNull(asValueOrNull(instance, specClass, marker)) { "value $instance is not a value with spec $specClass and marker $marker" }

inline fun <reified SPEC : Any> asValueOrNull(instance: Any, marker: AvMarker? = null): AvValue<SPEC>? =
    asValueOrNull(instance, SPEC::class, marker)

fun <SPEC : Any> asValueOrNull(instance: Any, specClass: KClass<SPEC>, marker: AvMarker? = null): AvValue<SPEC>? {
    return (instance as? AvValue<SPEC>)?.let {
        if (specClass.isInstance(instance.spec) && (marker == null || marker in instance.markers)) it else null
    }
}