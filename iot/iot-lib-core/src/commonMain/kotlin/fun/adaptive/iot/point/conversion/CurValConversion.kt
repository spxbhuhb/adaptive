package `fun`.adaptive.iot.point.conversion

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.value.AvValue

abstract class CurValConversion : AdatClass {

    open fun convert(curVal: AvValue): AvValue = curVal

}