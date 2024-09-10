package `fun`.adaptive.cookbook.form

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties

@Adat
class FormData(
    val boolean : Boolean = true,
    val byte : Byte = 12,
    val short : Short = 23,
    val int : Int = 34,
    val long : Long = 45,
    val float : Float = 67.8f,
    val double : Double = 89.10,
    val char : Char = 'A',
    val string : String = "ab",
) {
    override fun descriptor() {
        properties {
            int maximum 100 minimum 10
            string pattern "[a-z]+"
        }
    }
}