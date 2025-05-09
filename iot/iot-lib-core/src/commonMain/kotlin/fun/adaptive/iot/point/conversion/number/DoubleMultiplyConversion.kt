package `fun`.adaptive.iot.point.conversion.number

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.point.conversion.CurValConversion
import `fun`.adaptive.value.AvValue2
import `fun`.adaptive.value.builtin.AvDouble

@Adat
class DoubleMultiplyConversion(
    val multiplier: Double
) : CurValConversion() {

    override fun convert(curVal: AvValue2): AvValue2 {
        if (curVal !is AvDouble) return curVal
        return curVal.copy(value = curVal.value * multiplier)
    }

}