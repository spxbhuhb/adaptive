package `fun`.adaptive.sandbox.recipe.ui.form

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties
import `fun`.adaptive.sandbox.support.E
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.theme.colors

@Adat
class FormData(
    val boolean: Boolean = true,
    val int: Int = 34,
    val long: Long = 45,
    val double: Double = 89.10,
    val string: String = "ab",
    val enum: E = E.V1,
    val enumOrNull: E? = null,
    val badges : Set<String> = setOf("badge1", "badge2")
) {
    override fun descriptor() {
        properties {
            int maximum 100 minimum 10
            string pattern "[a-z]+"
        }
    }
}