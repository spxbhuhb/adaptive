package `fun`.adaptive.cookbook.auth

import `fun`.adaptive.cookbook.auth.screens.passwordReset
import `fun`.adaptive.cookbook.auth.screens.signIn
import `fun`.adaptive.cookbook.auth.screens.signUp
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun authRecipe() {
    column {
        maxHeight .. verticalScroll

        flowBox {
            maxWidth .. gap { 16.dp }

            signUp()
            signIn()
            passwordReset()
        }
    }
}
