package `fun`.adaptive.value.persistence

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId

abstract class AbstractValuePersistence {

    abstract fun loadValues(map: MutableMap<AvValueId, AvValue>)

    abstract fun saveValue(value: AvValue)

}