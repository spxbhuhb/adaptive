package `fun`.adaptive.resource

import kotlin.reflect.KProperty

typealias ResourceKey = String
typealias StringResourceKey = ResourceKey
typealias GraphicsResourceKey = ResourceKey

/**
 * Get the resource key for the given property. For example, `Strings::exampleMessage` returns
 * with the resource key of `exampleMessage` which you can use later with [resolveString](function://).
 */
fun resourceKey(property : KProperty<*>) : ResourceKey {
    return property.name
}