package `fun`.adaptive.cookbook.auth

import `fun`.adaptive.cookbook.auth.screens.passwordReset
import `fun`.adaptive.cookbook.auth.screens.signIn
import `fun`.adaptive.cookbook.auth.screens.welcome
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.instruction.AdaptiveInstruction
import `fun`.adaptive.ui.common.fragment.flowBox
import `fun`.adaptive.ui.common.instruction.dp
import `fun`.adaptive.ui.common.instruction.gap


@Adaptive
fun authMain(vararg instructions: AdaptiveInstruction) {
    flowBox {
        gap { 16.dp }

        welcome()
        signIn()
        passwordReset()
    }
}
