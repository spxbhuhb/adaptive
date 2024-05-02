package hu.simplexion.adaptive.server.setting.dsl

import hu.simplexion.adaptive.utility.Lock
import hu.simplexion.adaptive.utility.use
import kotlin.reflect.KProperty


 class SettingDelegate<T>(
     val path: String,
    val encoder: (value: T) -> String,
    val decoder: (value: String?) -> T
) {

     var sensitive: Boolean = false
     var writable: Boolean = true

    var initialized = false

    // FIXME settings default mess
    var default: T? = null

    val lock = Lock()

     var valueOrNull: T? = null
        get() {
            lock.use {
                if (! initialized) field = initialize()
                return field
            }
        }
        set(v) {
            lock.use {
                initialized = true
                field = v
            }
        }

     var value: T
        get() = checkNotNull(valueOrNull) { "missing setting: path=$path" }
        set(value) {
            valueOrNull = value
        }

     operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        value

    fun initialize(): T? =
        defaultSettingProvider.get(path).firstOrNull()?.value?.let { decoder(it) } ?: default

     operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        defaultSettingProvider.put(path, encoder(value))
    }

     infix fun default(value: T): SettingDelegate<T> {
        this.default = value
        return this
    }

}