package hu.simplexion.adaptive.settings.dsl

import hu.simplexion.adaptive.settings.provider.DelegatingSettingProvider
import hu.simplexion.adaptive.settings.provider.InlineSettingProvider
import hu.simplexion.adaptive.settings.provider.SettingProvider
import kotlin.reflect.KClass

inline fun <reified T : Any> setting(noinline path: () -> String): SettingDelegate<T> =
    setting(T::class, path())

expect fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T>

fun settings(providerFun: DelegatingSettingProvider.() -> Unit) {
    defaultSettingProvider.providerFun()
}

/**
 * Adds an inline setting provider which contains the settings passed in [values].
 *
 * ```kotlin
 * settings { inline( "EMAIL_USERNAME" to "test@localhost") }
 * ```
 */
fun DelegatingSettingProvider.inline(vararg values: Pair<String, Any?>) {
    this += InlineSettingProvider().also { it.items.plus(values) }
}