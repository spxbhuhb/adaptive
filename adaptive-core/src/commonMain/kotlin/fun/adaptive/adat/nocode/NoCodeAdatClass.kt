package `fun`.adaptive.adat.nocode

import `fun`.adaptive.adat.AdatClass

class NoCodeAdatClass(
    override val adatCompanion: NoCodeAdatCompanion,
    val values: Array<Any?>
) : AdatClass {

    override fun genGetValue(index: Int): Any? = values[index]

    override fun genSetValue(index: Int, value: Any?) {
        values[index] = value
    }
}