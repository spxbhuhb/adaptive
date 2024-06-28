/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.adaptive.foundation.Adaptive
import hu.simplexion.adaptive.foundation.instruction.AdaptiveDetach
import hu.simplexion.adaptive.foundation.instruction.DetachHandler
import hu.simplexion.adaptive.foundation.instruction.DetachName
import hu.simplexion.adaptive.foundation.instruction.name
import hu.simplexion.adaptive.foundation.rangeTo
import hu.simplexion.adaptive.resource.DrawableResource
import hu.simplexion.adaptive.site.*
import hu.simplexion.adaptive.ui.common.AbstractCommonAdapter
import hu.simplexion.adaptive.ui.common.browser
import hu.simplexion.adaptive.ui.common.fragment.*
import hu.simplexion.adaptive.ui.common.instruction.*
import hu.simplexion.adaptive.ui.common.platform.mediaMetrics
import hu.simplexion.adaptive.ui.common.platform.withJsResources
import hu.simplexion.adaptive.wireformat.withJson

val black = Color(0x000000u)
val white = Color(0xffffffu)
val gray = Color(0x606060u)

val whiteBackground = BackgroundColor(black)
val blackBackground = BackgroundColor(black)

val titleLarge = FontSize(36.sp)
val titleMedium = FontSize(20.sp)
val titleSmall = FontSize(16.sp)

val shadow = dropShadow(Color(0xc0c0c0u), 3.dp, 3.dp, 3.dp)

fun main() {

    withJson()
    withJsResources()

    browser {
        with(it as AbstractCommonAdapter<*, *>) {
            defaultTextRenderData.fontName = "Noto Sans"
            defaultTextRenderData.fontSize = 16.sp
        }

        val media = mediaMetrics()
        val background = if (media.isLight) whiteBackground else blackBackground

        grid(background) {
            colTemplate(1.fr)
            rowTemplate(48.dp, 32.dp, 1.fr)

            header()
            row { }
            column {
                maxSize .. verticalScroll

                slot(mainContent) { cards() }
            }
        }
    }
}

val mainContent = name("main content")

fun replaceMain(
    @DetachName segment: String,
    @AdaptiveDetach content: (handler: DetachHandler) -> Unit
) = NavClick(mainContent, segment, content)

private val grid1fr = arrayOf(
    rowTemplate(1.fr),
    colTemplate(1.fr)
)

@Adaptive
fun header() {
    row {
        maxSize .. AlignItems.center .. blackBackground

        text("Adaptive", white)
    }
}

@Adaptive
fun hello() {
    text("Hello World!")
}

@Adaptive
fun cards() {
    val media = mediaMetrics()

    row {
        maxWidth
        AlignItems.topCenter

        flowBox {
            flowItemLimit { 4 }
            gap(24.dp)

            card(Res.drawable.what_is_adaptive, Color(0xFFF7E1u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/what-is-adaptive.md")
                    largeTitle("What is Adaptive")
                    mediumTitle("main concept")
                    mediumTitle("features")
                    mediumTitle("goals")
                }
            }

            card(Res.drawable.preview_status, Color(0xFF6347u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/status.md")
                    largeTitle("Status")
                    mediumTitle("preview")
                    mediumTitle("web, Android, iOS - PoC works")
                    mediumTitle("roadmap")
                }
            }

            card(Res.drawable.getting_started, Color(0x87CEFAu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/getting-started.md")
                    largeTitle("Getting Started")
                    mediumTitle("demo")
                    mediumTitle("tutorial")
                    mediumTitle("documentation")
                }
            }

            card(Res.drawable.tools, Color(0xFFB07Cu)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/tools.md")
                    largeTitle("Tools")
                    inactiveFeature("project wizard", "2024.08")
                    inactiveFeature("UI designer", "2024.10")
                    inactiveFeature("AI integration", "Christmas")
                }
            }

            card(Res.drawable.ui, Color(0xFFBF00u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/ui/README.md")
                    largeTitle("User Interface")
                    mediumTitle("multiplatform")
                    mediumTitle("reactive")
                    mediumTitle("pretty")
                }
            }

            card(Res.drawable.server, Color(0x3CB371u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/server/README.md")
                    largeTitle("Server & Cloud")
                    mediumTitle("multiplatform")
                    mediumTitle("reactive")
                    mediumTitle("API driven")
                }
            }

            card(Res.drawable.impressum, Color(0xAEE1E1u)) {
                column {
                    externalLink("https://github.com/spxbhuhb/adaptive/blob/main/doc/impressum.md")
                    largeTitle("Impressum")
                    mediumTitle("motivation")
                    mediumTitle("credits")
                    mediumTitle("license")
                }
            }

            card(Res.drawable.deep_waters, Color(0x6495EDu)) {
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
    image: DrawableResource,
    background: Color,
    @Adaptive content: () -> Unit
) {
    val media = mediaMetrics()
    val shadowIfLight = if (media.isLight) arrayOf(shadow) else emptyArray()

    column(*shadowIfLight) {
        grid(size(360.dp, 214.dp)) {
            grid1fr
            padding(32.dp)
            cornerTopRadius(16.dp)
            backgroundColor(background)
            content()
        }
        image(
            image,
            size(360.dp, (360 * 1024 / 1792).dp),
            cornerBottomRadius(16.dp)
        )
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

        text(name, titleMedium, gray, TextWrap.NoWrap)
        text("ETA: $eta", titleSmall, gray, TextWrap.NoWrap) .. AlignSelf.bottom
    }
}