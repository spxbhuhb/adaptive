package `fun`.adaptive.cookbook.auth

import `fun`.adaptive.cookbook.auth.ui.account.accounts
import `fun`.adaptive.cookbook.auth.ui.large.authLarge
import `fun`.adaptive.cookbook.auth.ui.small.authSmall
import `fun`.adaptive.cookbook.auth.ui.small.passwordReset
import `fun`.adaptive.cookbook.auth.ui.small.signIn
import `fun`.adaptive.cookbook.auth.ui.small.signUp
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.button.api.button
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr
import `fun`.adaptive.ui.navigation.NavState

val authRouting = NavState("Auth")

private val small = NavState(listOf("Auth", "small"))
private val large = NavState(listOf("Auth", "large"), fullScreen = true)
private val users = NavState(listOf("Auth", "users"))

@Adaptive
fun authRecipe(navState: NavState?) {
    grid {
        maxSize .. rowTemplate(60.dp, 1.fr) .. gap { 16.dp }

        row {
            gap { 16.dp }
            button("Small") .. onClick { navState?.goto(small) }
            button("Large") .. onClick { navState?.goto(large) }
            button("Users") .. onClick { navState?.goto(users) }
        }

        when (navState) {
            in small -> authSmall()
            in large -> authLarge()
            in users -> accounts()
            else -> text("todo")
        }
    }
}




