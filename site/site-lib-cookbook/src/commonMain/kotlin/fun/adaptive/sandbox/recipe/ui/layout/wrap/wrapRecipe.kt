package `fun`.adaptive.cookbook.recipe.ui.layout.wrap

import `fun`.adaptive.cookbook.support.example
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.*
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.theme.backgrounds
import `fun`.adaptive.ui.wrap.wrapFromBottom
import `fun`.adaptive.ui.wrap.wrapFromLeft
import `fun`.adaptive.ui.wrap.wrapFromRight
import `fun`.adaptive.ui.wrap.wrapFromTop

@Adaptive
fun wrapRecipe(): AdaptiveFragment {

    val wrapperSize = 8.dp

    column {
        maxSize .. verticalScroll .. horizontalScroll // this makes the examples use their smallest size
        gap { 24.dp }

        example("wrap from left") {
            wrapFromLeft(wrapperSize, ::leftWrapper) {
                text("Hello World!")
            }
        }

        example("double wrap from left") {
            wrapFromLeft(wrapperSize, ::leftWrapper) {
                wrapFromLeft(wrapperSize, ::leftWrapper) {
                    text("Hello World!")
                }
            }
        }

        example("wrap from top") {
            wrapFromTop(wrapperSize, ::topWrapper) {
                text("Hello World!")
            }
        }

        example("double wrap from top") {
            wrapFromTop(wrapperSize, ::topWrapper) {
                wrapFromTop(wrapperSize, ::topWrapper) {
                    text("Hello World!")
                }
            }
        }

        example("wrap from right") {
            wrapFromRight(wrapperSize, ::rightWrapper) {
                text("Hello World!")
            }
        }

        example("double wrap from right") {
            wrapFromRight(wrapperSize, ::rightWrapper) {
                wrapFromRight(wrapperSize, ::rightWrapper) {
                    text("Hello World!")
                }
            }
        }

        example("wrap from bottom") {
            wrapFromBottom(wrapperSize, ::bottomWrapper) {
                text("Hello World!")
            }
        }

        example("double wrap from bottom") {
            wrapFromBottom(wrapperSize, ::bottomWrapper) {
                wrapFromBottom(wrapperSize, ::bottomWrapper) {
                    text("Hello World!")
                }
            }
        }

        example("wrap all around") {
            wrapFromLeft(wrapperSize, ::leftWrapper) {
                wrapFromTop(wrapperSize, ::topWrapper) {
                    wrapFromRight(wrapperSize, ::rightWrapper) {
                        wrapFromBottom(wrapperSize, ::bottomWrapper) {
                            text("Hello World!")
                        }
                    }
                }
            }
        }
    }

    return fragment()
}

@Adaptive
private fun leftWrapper(): AdaptiveFragment {
    box { maxHeight .. width { 8.dp } .. marginRight { 4.dp } .. backgrounds.successSurface .. cornerRadius { 2.dp } }
    return fragment()
}

@Adaptive
private fun topWrapper(): AdaptiveFragment {
    box { maxWidth .. height { 8.dp } .. marginBottom { 4.dp } .. backgrounds.infoSurface .. cornerRadius { 2.dp } }
    return fragment()
}

@Adaptive
private fun rightWrapper(): AdaptiveFragment {
    box { maxHeight .. width { 8.dp } .. marginLeft { 4.dp } .. backgrounds.warningSurface .. cornerRadius { 2.dp } }
    return fragment()
}

@Adaptive
private fun bottomWrapper(): AdaptiveFragment {
    box { maxWidth .. height { 8.dp } .. marginTop { 4.dp } .. backgrounds.failSurface .. cornerRadius { 2.dp } }
    return fragment()
}