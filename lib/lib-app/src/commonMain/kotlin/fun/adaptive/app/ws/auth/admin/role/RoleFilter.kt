package `fun`.adaptive.app.ws.auth.admin.role

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auth.model.RoleSpec
import `fun`.adaptive.value.AvValue

@Adat
data class RoleFilter(
    val text: String = "",
) {
    fun isEmpty() = text.isEmpty()

    fun matches(item: AvValue<RoleSpec>): Boolean =
        (
            text.isEmpty()
                || item.name.contains(text, ignoreCase = true)
                || item.spec.context?.contains(text, ignoreCase = true) == true
            )
}