package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.layout.AlignItems
import `fun`.adaptive.ui.instruction.text.FontName
import `fun`.adaptive.ui.instruction.text.FontSize
import `fun`.adaptive.ui.api.externalLink
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.api.underline


@Adaptive
fun footerLink(normalText: String, linkText: String, href: String) {
    row(AlignItems.center, maxWidth) {
        text(normalText, *bodyMedium)
        text(linkText, FontSize(17.sp), textColor(black), underline, externalLink(href))
    }
}