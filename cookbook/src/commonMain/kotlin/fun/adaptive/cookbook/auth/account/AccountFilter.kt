package `fun`.adaptive.cookbook.auth.account

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auth.model.Principal
import `fun`.adaptive.cookbook.auth.model.AccountSummary
import kotlin.text.contains
import kotlin.text.isEmpty

@Adat
data class AccountFilter(
    val text: String = "",
) {
    fun isEmpty() = text.isEmpty()

    fun matches(item: AccountSummary): Boolean =
        (text.isEmpty() || item.name.contains(text, ignoreCase = true))
}