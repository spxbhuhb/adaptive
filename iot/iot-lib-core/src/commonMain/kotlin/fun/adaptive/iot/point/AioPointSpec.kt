package `fun`.adaptive.iot.point

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.iot.point.conversion.CurValConversion

abstract class AioPointSpec : AdatClass {
    abstract val displayAddress: String
    abstract val notes: String
    abstract val conversion: CurValConversion?
}