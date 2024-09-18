package `fun`.adaptive.atomic

import `fun`.adaptive.utility.Lock
import `fun`.adaptive.utility.getLock
import `fun`.adaptive.utility.use
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Atomic<T>(
    initialValue: T,
    private val lock : Lock = getLock()
) {

    private var value = initialValue

    fun get(): T {
        return lock.use { value }
    }

    fun set(value: T) {
        lock.use { this.value = value }
    }

    fun compute(computeFun : (it : T) -> T) : T =
        lock.use { computeFun(value).also { value = it } }

}