package `fun`.adaptive.iot.model

import `fun`.adaptive.adat.Adat

@Adat
class AioStatus(
    val flags: Int
) {

    companion object {
        val OK = AioStatus(0)
    }
}
