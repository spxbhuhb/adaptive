package `fun`.adaptive.cookbook.shared

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.common.fragment.row
import `fun`.adaptive.ui.common.fragment.text
import `fun`.adaptive.ui.common.instruction.AlignItems
import `fun`.adaptive.ui.common.instruction.FontName
import `fun`.adaptive.ui.common.instruction.FontSize
import `fun`.adaptive.ui.common.instruction.externalLink
import `fun`.adaptive.ui.common.instruction.maxWidth
import `fun`.adaptive.ui.common.instruction.sp
import `fun`.adaptive.ui.common.instruction.textColor
import `fun`.adaptive.ui.common.instruction.underline


@Adaptive
fun footerLink(normalText: String, linkText: String, href: String) {
    row(AlignItems.center, maxWidth) {
        text(normalText, *bodyMedium)
        text(linkText, FontSize(17.sp), FontName("Noto Sans"), textColor(black), underline, externalLink(href))
    }
}