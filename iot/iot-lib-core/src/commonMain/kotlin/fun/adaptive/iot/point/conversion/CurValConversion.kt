package `fun`.adaptive.iot.point.conversion

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.value.AvValue2

abstract class CurValConversion : AdatClass {

    open fun convert(curVal: AvValue2): AvValue2 = curVal

}