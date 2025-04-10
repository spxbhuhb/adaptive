/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.lib.sandbox.ui.mobile

import `fun`.adaptive.adat.store.copyOf
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.binding.PropertySelector
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.graphics.svg.api.svg
import `fun`.adaptive.lib.sandbox.model.SignUp
import `fun`.adaptive.resource.graphics.Graphics
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.boldFont
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.color
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.cornerRadius
import `fun`.adaptive.ui.api.externalLink
import `fun`.adaptive.ui.api.fontSize
import `fun`.adaptive.ui.api.frame
import `fun`.adaptive.ui.api.gapWidth
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.height
import `fun`.adaptive.ui.api.lightFont
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.noBorder
import `fun`.adaptive.ui.api.noSelect
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.paddingLeft
import `fun`.adaptive.ui.api.paddingRight
import `fun`.adaptive.ui.api.paddingTop
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.size
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.underline
import `fun`.adaptive.ui.generated.resources.check
import `fun`.adaptive.ui.editor.editor
import `fun`.adaptive.ui.instruction.*
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.layout.Height
import `fun`.adaptive.ui.instruction.text.FontName
import `fun`.adaptive.ui.instruction.text.FontSize

@Adaptive
fun welcome(withFeedback: Boolean = false) {
    val signUp = copyOf { SignUp() }

    if (withFeedback) {
        row {
            gapWidth { 24.dp }
            mobileExample { welcomeInner(signUp) }
            feedback(signUp)
        }
    } else {
        welcomeInner(signUp)
    }
}

@Adaptive
fun welcomeInner(signUp: SignUp) {
    grid {
        colTemplate(1.fr)
        rowTemplate(213.dp, 78.dp, 1.fr, 81.dp)
        paddingLeft(32.dp)
        paddingRight(32.dp)

        titleLarge("Welcome")

        subTitle("Sign up to join")

        grid {
            colTemplate(1.fr)
            rowTemplate(52.dp repeat 4, 60.dp, 50.dp)

            editor { signUp.name }
            editor { signUp.email }
            editor { signUp.password }
            editor { signUp.verification }

            grid {
                paddingTop(15.dp)
                AlignItems.Companion.start
                colTemplate(40.dp, 1.fr)

                checkbox { signUp.agreement }
                row {
                    text("I agree to the ", fontSize(15.sp), FontName("Noto Sans"), textColor(mediumGray))
                    text("Terms of Service", fontSize(15.sp), FontName("Noto Sans"), textColor(black), bold, externalLink("/terms.txt"))
                }
            }

            button("Sign Up") .. maxWidth .. onClick { println("sing up") }
        }

        footerLink("Have an account? ", "Sign in", "/")
    }
}

@Adaptive
fun titleLarge(text: String) {
    row(height(213.dp), paddingTop(117.dp)) {
        text(text, titleLarge)
    }
}

@Adaptive
fun subTitle(text: String) {
    text(text, bodyMedium, lightFont, paddingTop(15.dp))
}

@Adaptive
fun footerLink(normalText: String, linkText: String, href: String) {
    row(AlignItems.Companion.center, maxWidth) {
        text(normalText, bodyMedium)
        text(linkText, FontSize(17.sp), FontName("Noto Sans"), textColor(black), underline, externalLink(href))
    }
}

/**
 * Editor for a boolean.
 */
@Adaptive
fun checkbox(
    binding: AdaptiveStateVariableBinding<Boolean>? = null,
    @PropertySelector
    selector: () -> Boolean
) {
    checkNotNull(binding)

    row {
        onClick { binding.setValue(! binding.value, true) }

        if (binding.value) {
            box(activeCheckBox) {
                svg(Graphics.check) .. noSelect .. frame(1.dp, 1.dp, 18.dp, 18.dp)
            }
        } else {
            box(inactiveCheckBox) {

            }
        }
    }
}

val titleLarge = instructionsOf(
    FontSize(40.sp),
    boldFont
)

val bodyMedium = instructionsOf(
    //FontSize(17.sp),
    //FontName("Noto Sans"),
    textColor(0x666666u)
)

val input = instructionsOf(
    // PlaceholderColor(0x8A8A8F),
    textColor(0x000000u),
    backgroundColor(color(0xEFEFF4u)),
    cornerRadius(8.dp),
    noBorder,
    Height(44.dp),
    FontSize(17.sp),
    lightFont,
    padding(left = 16.dp, right = 16.dp)
)

var activeCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    backgroundColor(purple),
    textColor(white)
)

var inactiveCheckBox = instructionsOf(
    size(20.dp, 20.dp),
    cornerRadius(10.dp),
    border(purple, 1.dp)
)

@Adaptive
fun feedback(signUp: SignUp) {
    column {
        text("name: ${signUp.name}")
        text("email: ${signUp.email}")
        text("password: ${signUp.password}")
        text("verification: ${signUp.verification}")
        text("agreement: ${signUp.agreement}")
    }
}