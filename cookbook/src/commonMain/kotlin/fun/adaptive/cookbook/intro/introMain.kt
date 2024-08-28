package `fun`.adaptive.cookbook.intro

import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.cookbook.intro.model.IntroScreen
import `fun`.adaptive.cookbook.intro.model.IntroState
import `fun`.adaptive.cookbook.intro.screens.end
import `fun`.adaptive.cookbook.intro.screens.start
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.flowBox
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.maxHeight
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.verticalScroll
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun introMain() {
    val state = copyStore { IntroState(IntroScreen.Start) }

    column {
        maxHeight .. verticalScroll

        flowBox {
            maxWidth .. gap { 16.dp }

            when (state.screen) {
                IntroScreen.Start -> start(state)
                IntroScreen.End -> end(state)
            }
        }
    }
}
