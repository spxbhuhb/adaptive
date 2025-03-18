package `fun`.adaptive.iot.common

import `fun`.adaptive.adat.Adat

@Adat
class AioStatus(
    val flags: Int
) {

    companion object {
        val OK = AioStatus(0)
    }
}
