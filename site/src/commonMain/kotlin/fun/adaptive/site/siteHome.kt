package `fun`.adaptive.site

import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.resource.image.ImageResourceSet
import `fun`.adaptive.resource.image.Images
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.fragment.paragraph.items.LinkParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.items.TextParagraphItem
import `fun`.adaptive.ui.fragment.paragraph.model.Paragraph
import `fun`.adaptive.ui.fragment.paragraph.model.ParagraphInstructionSet
import `fun`.adaptive.ui.instruction.decoration.Color
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.layout.AlignSelf
import `fun`.adaptive.ui.instruction.sp
import `fun`.adaptive.ui.theme.backgrounds

@Adaptive
fun siteHome(): AdaptiveFragment {

    column {
        maxSize .. gap { 24.dp } .. paddingTop { 32.dp } .. verticalScroll

        text("Adaptive") .. fontSize { 48.sp } .. alignSelf.center

        box {
            width { 400.dp } .. alignSelf.center .. backgrounds.friendlyOpaque .. padding { 16.dp }
            cornerRadius { 4.dp } .. marginBottom { 16.dp }
            paragraph(p) .. alignSelf.center
        }

        cards()

        text("adaptive.fun does not use cookies") ..
            paddingTop { 32.dp } ..
            paddingBottom { 32.dp } ..
            alignSelf.center
    }

    return fragment()
}

val p = Paragraph(
    listOf(ParagraphInstructionSet(instructionsOf())),
    listOf(
        LinkParagraphItem("Check this video for a short walkthrough of this site.", "https://youtube.com", 0)
    )
)

val white = color(0xffffffu)
val gray = color(0x606060u)

val lightTextColor = textColor(0xffffffu)
val darkTextColor = textColor(0x2E2E2Eu)

val lightBackground = backgroundColor(0xffffffu)
val darkBackground = backgroundColor(0x2E2E2Eu)

val titleLarge = fontSize(36.sp)
val titleMedium = fontSize(20.sp)
val titleSmall = fontSize(16.sp)

val shadow = dropShadow(color(0xc0c0c0u), 3.dp, 3.dp, 3.dp)

@Adaptive
fun cards() {
    row {
        maxWidth .. alignItems.topCenter

        flowBox {
            flowItemLimit { 4 }
            gap(24.dp)

            card(Images.what_is_adaptive_50, color(0xFFF7E1u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/what-is-adaptive.md")
                    largeTitle("What is Adaptive")
                    mediumTitle("main concept")
                    mediumTitle("features")
                    mediumTitle("goals")
                }
            }

            card(Images.preview_status_50, color(0xFF6347u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/status.md")
                    largeTitle("Status")
                    mediumTitle("preview")
                    mediumTitle("web, Android, iOS - PoC works")
                    mediumTitle("roadmap")
                }
            }

            card(Images.getting_started_50, color(0x87CEFAu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/getting-started.md")
                    largeTitle("Getting Started")
                    mediumTitle("demo")
                    mediumTitle("tutorial")
                    mediumTitle("documentation")
                }
            }

            card(Images.tools_50, color(0xFFB07Cu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/tools.md")
                    largeTitle("Tools")
                    inactiveFeature("project wizard", "2025 Q3")
                    inactiveFeature("UI designer", "2025 Q1")
                    inactiveFeature("AI integration", "Christmas")
                }
            }

            card(Images.ui_50, color(0xFFBF00u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/ui/README.md")
                    largeTitle("User Interface")
                    mediumTitle("multiplatform")
                    mediumTitle("reactive")
                    mediumTitle("pretty")
                }
            }

            card(Images.server_50, color(0x3CB371u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/server/README.md")
                    largeTitle("Server & Cloud")
                    mediumTitle("multiplatform")
                    mediumTitle("reactive")
                    mediumTitle("API driven")
                }
            }

            card(Images.impressum_50, color(0xAEE1E1u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/impressum.md")
                    largeTitle("Impressum")
                    mediumTitle("motivation")
                    mediumTitle("credits")
                    mediumTitle("license")
                }
            }

            card(Images.deep_waters_50, color(0x6495EDu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/internals")
                    largeTitle("Deep Waters")
                    mediumTitle("internals")
                    mediumTitle("compiler plugin")
                    mediumTitle("source code")
                }
            }
        }
    }
}

@Adaptive
fun card(
    image: ImageResourceSet,
    background: Color,
    @Adaptive cardContent: () -> Unit
) {
    column {
        grid {
            size(360.dp, 214.dp) .. padding(32.dp) .. cornerTopRadius(16.dp) .. backgroundColor(background)

            cardContent()
        }

        image(image) .. size(360.dp, (360 * 1024 / 1792).dp) .. cornerBottomRadius(16.dp)
    }
}

@Adaptive
fun largeTitle(content: String) {
    text(content, titleLarge, paddingBottom(16.dp))
}

@Adaptive
fun mediumTitle(content: String) {
    text(content, titleMedium)
}

@Adaptive
fun inactiveFeature(name: String, eta: String) {
    row {
        maxWidth .. spaceBetween
        gap(10.dp)

        text(name, titleMedium, textColor(gray), noTextWrap)
        text("ETA: $eta", titleSmall, textColor(gray), noTextWrap) .. AlignSelf.Companion.bottom
    }
}