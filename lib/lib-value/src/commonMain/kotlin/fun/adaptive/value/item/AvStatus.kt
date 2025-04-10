package `fun`.adaptive.value.item

import `fun`.adaptive.adat.Adat

@Adat
class AvStatus(
    val flags: Int
) {

    val isOk
        get() = (flags == OK.flags)

    val isActive
        get() = (flags == 0)

    val isDown
        get() = (flags != 0)

    val isAlarm
        get() = (flags != 0)

    companion object {
        val OK = AvStatus(0)
    }
}
