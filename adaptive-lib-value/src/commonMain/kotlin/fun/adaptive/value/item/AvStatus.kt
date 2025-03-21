package `fun`.adaptive.value.item

import `fun`.adaptive.adat.Adat

@Adat
class AvStatus(
    val flags: Int
) {

    val isOk
        get() = (flags == OK.flags)

    companion object {
        val OK = AvStatus(0)
    }
}
