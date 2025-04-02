package `fun`.adaptive.app.ws.auth.admin

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auth.model.basic.BasicAccountSummary

@Adat
data class AccountFilter(
    val text: String = "",
) {
    fun isEmpty() = text.isEmpty()

    fun matches(item: BasicAccountSummary): Boolean =
        (text.isEmpty() || item.name.contains(text, ignoreCase = true) || item.email.contains(text, ignoreCase = true))
}