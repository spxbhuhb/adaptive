package `fun`.adaptive.app.basic.auth.ui

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.app.basic.auth.model.BasicAccountSummary

@Adat
data class AccountFilter(
    val text: String = "",
) {
    fun isEmpty() = text.isEmpty()

    fun matches(item: BasicAccountSummary): Boolean =
        (text.isEmpty() || item.name.contains(text, ignoreCase = true))
}