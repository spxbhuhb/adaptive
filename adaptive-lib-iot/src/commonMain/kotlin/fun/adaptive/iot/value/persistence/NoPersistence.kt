package `fun`.adaptive.iot.value.persistence

import `fun`.adaptive.iot.value.AioValue
import `fun`.adaptive.iot.value.AioValueId

class NoPersistence : AbstractValuePersistence() {

    override fun loadValues(map: MutableMap<AioValueId, AioValue>) = Unit

    override fun saveValue(value: AioValue) = Unit

}