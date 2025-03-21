package `fun`.adaptive.value.persistence

import `fun`.adaptive.value.AvValue
import `fun`.adaptive.value.AvValueId

class NoPersistence : AbstractValuePersistence() {

    override fun loadValues(map: MutableMap<AvValueId, AvValue>) = Unit

    override fun saveValue(value: AvValue) = Unit

}