package hu.simplexion.adaptive.settings.dsl

import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
actual fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T> =
    // FIXME add setting type support to the compiler plugin
    when (kClass) {
        Boolean::class -> SettingDelegate(path, { it.toString() }, { it !!.toBoolean() })
        Int::class -> SettingDelegate(path, { it.toString() }, { it !!.toInt() })
        String::class -> SettingDelegate(path, { it }, { it !! })
        else -> throw NotImplementedError("not implemented setting class: $kClass")
    } as SettingDelegate<T>

// ---------------------------------------------------------------------
// Setting provider builders
// ---------------------------------------------------------------------

suspend fun settingsFromServer() {
    TODO()
}