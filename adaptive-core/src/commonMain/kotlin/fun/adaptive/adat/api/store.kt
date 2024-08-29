package `fun`.adaptive.adat.api

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.AdatContext
import `fun`.adaptive.utility.PluginReference
import `fun`.adaptive.utility.pluginGenerated

/**
 * Update a field of an immutable Adat instance. This function is meant
 * for use cases where the adat class has a store ([AdatContext.store])
 * and the update is handled by the store.
 */
@PluginReference
fun <T> T.update(value: () -> T) {
    pluginGenerated(value)
}

/**
 * Update a field of an immutable Adat instance. This function is meant
 * for use cases where the adat class has a store ([AdatContext.store])
 * and the update is handled by the store.
 */
@PluginReference
fun <T> update(instance: AdatClass<*>, path: Array<String>, value: () -> T) {
    val store = requireNotNull(instance.adatContext?.store) { "there is no store for the update" }
    store.update(instance, path, value())
}

