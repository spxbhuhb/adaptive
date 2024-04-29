package hu.simplexion.adaptive.settings.dsl

import hu.simplexion.adaptive.settings.provider.EnvironmentSettingProvider
import hu.simplexion.adaptive.settings.provider.PropertyFileSettingProvider
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

// ---------------------------------------------------------------------
// Setting value builders
// ---------------------------------------------------------------------


@Suppress("UNCHECKED_CAST")
actual fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T> =
    // FIXME add setting type support to the compiler plugin
    when (kClass) {
        Boolean::class -> SettingDelegate(path, { it.toString() }, { it !!.toBoolean() })
        Int::class -> SettingDelegate(path, { it.toString() }, { it !!.toInt() })
        Path::class -> SettingDelegate(path, { it.toString() }, { Paths.get(it !!) })
        String::class -> SettingDelegate(path, { it }, { it !! })
        else -> throw NotImplementedError("not implemented setting class: $kClass")
    } as SettingDelegate<T>

// ---------------------------------------------------------------------
// Setting provider builders
// ---------------------------------------------------------------------


fun environment(prefix: () -> String): EnvironmentSettingProvider =
    EnvironmentSettingProvider(prefix())


fun propertyFile(optional: Boolean = true, path: () -> String) =
    PropertyFileSettingProvider(Paths.get(path()), optional)