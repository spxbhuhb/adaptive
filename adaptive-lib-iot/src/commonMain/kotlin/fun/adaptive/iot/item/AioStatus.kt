package `fun`.adaptive.iot.item

import `fun`.adaptive.adat.Adat

@Adat
class AioStatus(
    val flags: Int
) {

    val isOk
        get() = (flags == OK.flags)

    companion object {
        val OK = AioStatus(0)
    }
}
