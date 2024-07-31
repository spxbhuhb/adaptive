package hu.simplexion.adaptive.auto.backend

import hu.simplexion.adaptive.adat.AdatChange
import hu.simplexion.adaptive.adat.AdatClass
import hu.simplexion.adaptive.adat.deepCopy
import hu.simplexion.adaptive.auto.ItemId
import hu.simplexion.adaptive.auto.LamportTimestamp
import hu.simplexion.adaptive.utility.UUID

class AutoInstance<A : AdatClass<A>>(
    override val globalId: UUID<AutoBackend>,
    time: LamportTimestamp,
    initialValue: A,
    val onChange: ((newValue: A) -> Unit)? = null
) : AutoBackend(time) {

    var value = initialValue

    val propertyTimes = Array<LamportTimestamp>(initialValue.getMetadata().properties.size) { time }

    // --------------------------------------------------------------------------------
    // Incoming from other backends
    // --------------------------------------------------------------------------------

    override fun modify(timestamp: LamportTimestamp, item: ItemId, propertyName: String, propertyValue: Any?) {

        val propertyIndex = value.adatIndexOf(propertyName)

        if (timestamp > propertyTimes[propertyIndex]) {
            propertyTimes[propertyIndex] = timestamp
            value = value.deepCopy(AdatChange(listOf(propertyName), propertyValue))
            onChange?.invoke(value)
        }
    }

    // --------------------------------------------------------------------------------
    // Changes from the frontend
    // --------------------------------------------------------------------------------

    fun modify(itemId: ItemId, propertyName: String, propertyValue: Any?) {
        val propertyIndex = value.adatIndexOf(propertyName)
        val timestamp = nextTime()

        propertyTimes[propertyIndex] = timestamp
        value = value.deepCopy(AdatChange(listOf(propertyName), propertyValue))

        onChange?.invoke(value)

        for (connector in connectors) {
            connector.modify(timestamp, itemId, propertyName, propertyValue)
        }
    }

}