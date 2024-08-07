package hu.simplexion.adaptive.atomic

import hu.simplexion.adaptive.utility.getLock
import hu.simplexion.adaptive.utility.use
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Atomic<T>(
    initialValue: T,
) : ReadWriteProperty<Any?, T> {

    private val lock = getLock()

    private var value = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return lock.use { value }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        lock.use { this.value = value }
    }

}