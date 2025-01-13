package `fun`.adaptive.cookbook.intro.screens

import `fun`.adaptive.cookbook.intro.components.container
import `fun`.adaptive.cookbook.intro.model.IntroState
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.maxWidth

@Adaptive
fun start(state: IntroState) {

    container(state, "What is Adaptive?") {

        column {

            flowText("A software suite for building full-stack Kotlin Multiplatform applications.") ..
                maxWidth

            flowText("A software suite for building full-stack Kotlin Multiplatform applications.") ..
                maxWidth

        }
    }

}