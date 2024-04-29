package hu.simplexion.adaptive.settings.dsl

import kotlin.reflect.KProperty

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SettingDelegate<T>(
    actual val path: String,
    val encoder: (value: T) -> String,
    val decoder: (value: String?) -> T
) {

    actual var sensitive: Boolean = false
    actual var writable: Boolean = true

    var initialized = false

    // FIXME settings default mess
    var default: T? = null

    actual var valueOrNull: T? = null
        get() {
            if (!initialized) field = initialize()
            return field
        }
        set(v) {
            initialized = true
            field = v
        }

    actual var value: T
        get() = checkNotNull(valueOrNull) { "missing setting: path=$path" }
        set(value) {
            valueOrNull = value
        }

    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        value

    fun initialize(): T? =
        defaultSettingProvider.get(path).firstOrNull()?.value?.let { decoder(it) } ?: default

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        defaultSettingProvider.put(path, encoder(value))
    }

    actual infix fun default(value: T): SettingDelegate<T> {
        this.default = value
        return this
    }

}