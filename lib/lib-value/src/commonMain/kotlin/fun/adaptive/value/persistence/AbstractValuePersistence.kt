package `fun`.adaptive.value.persistence

import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.AvValueId

abstract class AbstractValuePersistence {

    abstract fun loadValues(map: MutableMap<AvValueId, AvValue2>)

    abstract fun saveValue(value: AvValue2)

}