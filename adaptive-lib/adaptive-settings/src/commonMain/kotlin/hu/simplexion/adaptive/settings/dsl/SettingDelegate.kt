package hu.simplexion.adaptive.settings.dsl

import kotlin.reflect.KProperty

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SettingDelegate<T> {

    val path: String

    var sensitive: Boolean
    var writable: Boolean

    var valueOrNull: T?
    var value: T

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)

    infix fun default(value: T): SettingDelegate<T>

}