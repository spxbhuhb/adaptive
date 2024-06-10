import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.sandbox.api.SignUp
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*

@Adaptive
fun welcome() {
    val signUp = SignUp("name", "email", "password", "verification")

    grid(
        colTemplate(1.fr),
        paddingLeft(32.dp),
        paddingRight(32.dp),
    ) {

        row { }

        row(AlignItems.Start) {
            text("Welcome", *titleMedium)
        }

        text("Sign up to join", *bodyMedium)

        grid(
            colTemplate(1.fr),
            rowTemplate(Repeat(4, 52.dp))
        ) {
            stringEditor(*editor) { signUp.name }
            stringEditor(*editor) { signUp.email }
            stringEditor(*editor) { signUp.password }
            stringEditor(*editor) { signUp.verification }
        }

        text("Agreement")

        button("Sign Up", Height(50.dp), onClick { println("sing up") })

        row(JustifyContent.Center) {
            text("Have an account?", *bodyMedium)
            text("Sign in", *bodyMedium, onClick { println("sign in") })
        }
    }
}

val titleMedium = arrayOf(
    FontSize(40.sp),
    FontWeight.BOLD
)

val bodyMedium = arrayOf(
    FontSize(17.sp),
    Color(0x666666)
)

val editor = arrayOf(
    // PlaceholderColor(0x8A8A8F),
    Color(0x000000),
    BackgroundColor(Color(0xEFEFF4)),
    BorderRadius(8.dp),
    Border.NONE,
    Height(44.dp),
    FontSize(17.sp),
    FontWeight.LIGHT,
    Padding(left = 16.dp, right = 16.dp)
)