package hu.simplexion.adaptive.settings.dsl

import hu.simplexion.adaptive.settings.provider.SettingProvider
import kotlin.reflect.KClass

inline fun <reified T : Any> setting(noinline path: () -> String): SettingDelegate<T> =
    setting(T::class, path())

expect fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T>

fun settings(providerFun : () -> SettingProvider) {
    defaultSettingProvider += providerFun()
}