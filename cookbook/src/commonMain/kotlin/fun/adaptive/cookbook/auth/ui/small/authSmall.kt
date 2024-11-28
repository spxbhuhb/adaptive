package `fun`.adaptive.cookbook.auth.ui.small

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxSize
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun authSmall() {
    flowBox {
        maxSize .. verticalScroll .. gap { 16.dp }

        signUp()
        smallSignIn()
        passwordReset()
    }
}