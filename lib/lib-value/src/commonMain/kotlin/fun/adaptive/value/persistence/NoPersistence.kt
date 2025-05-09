package `fun`.adaptive.value.persistence

import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.AvValueId

class NoPersistence : AbstractValuePersistence() {

    override fun loadValues(map: MutableMap<AvValueId, AvValue2>) = Unit

    override fun saveValue(value: AvValue2) = Unit

}