package `fun`.adaptive.backend.setting.dsl

import `fun`.adaptive.backend.setting.provider.DelegatingSettingProvider
import `fun`.adaptive.backend.setting.provider.InlineSettingProvider
import kotlin.reflect.KClass

inline fun <reified T : Any> setting(noinline path: () -> String): SettingDelegate<T> =
    setting(T::class, path())

@Suppress("UNCHECKED_CAST")
fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T> =
    // FIXME add setting type support to the compiler plugin
    when (kClass) {
        Boolean::class -> SettingDelegate(path, { it.toString() }, { it !!.toBoolean() })
        Int::class -> SettingDelegate(path, { it.toString() }, { it !!.toInt() })
        Long::class -> SettingDelegate(path, { it.toString() }, { it !!.toLong() })
        String::class -> SettingDelegate(path, { it }, { it !! })
        else -> throw NotImplementedError("not implemented setting class: $kClass")
    } as SettingDelegate<T>

fun settings(providerFun: DelegatingSettingProvider.() -> Unit) {
    defaultSettingProvider.providerFun()
}

/**
 * Adds an inline setting provider which contains the settings passed in [values].
 *
 * ```kotlin
 * settings { inline("EMAIL_USERNAME" to "test@localhost") }
 * ```
 */
fun DelegatingSettingProvider.inline(vararg values: Pair<String, Any?>) {
    this += InlineSettingProvider().also { p ->
        p.items.putAll(values.map { Pair(it.first, it.second.toString()) })
    }
}