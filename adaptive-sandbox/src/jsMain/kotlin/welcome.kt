import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.Name
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.sandbox.api.SignUp
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*

@Adaptive
fun editors() {
    val signUp = SignUp(name = "Hello")

    grid(
        colTemplate(1.fr),
        rowTemplate(1.fr, 381.dp, 1.fr),
        padding(32.dp)
    ) {
        row(AlignItems.Center) {
            text("Welcome", *titleMedium)
        }

        column {
            text("Sign up to join", *bodyMedium)

            stringEditor(*editor) { signUp.name }
            stringEditor(*editor) { signUp.email }
            stringEditor(*editor) { signUp.password }
            stringEditor(*editor) { signUp.verification }

            button("Sign Up", *button, onClick { println("sing up") })
        }

        row(*center) {
            text("Have an account?", *bodyMedium)
            text("Sign in", *bodyMedium, onClick { println("sign in") })
        }
    }
}

val button = arrayOf(
    greenGradient,
    borderRadius,
    *center
)

val titleMedium = arrayOf(
    FontSize(40.sp),
    FontWeight.BOLD
)

val bodyMedium = arrayOf(
    FontSize(17.sp),
    Color(0x666666)
)

val editor = arrayOf(
    Color(0x8A8A8F),
    BackgroundColor(Color(0xEFEFF4)),
    BorderRadius(8.dp),
    Border.NONE,
    Height(44.dp),
    FontSize(17.sp),
    FontWeight.LIGHT,
    Padding(left = 16.dp, right = 16.dp)
)