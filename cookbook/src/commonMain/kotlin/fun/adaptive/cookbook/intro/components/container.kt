package `fun`.adaptive.cookbook.intro.components

import `fun`.adaptive.cookbook.intro.model.IntroState
import `fun`.adaptive.cookbook.shared.button
import `fun`.adaptive.cookbook.shared.mobileScreen
import `fun`.adaptive.cookbook.shared.title
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.backgroundColor
import `fun`.adaptive.ui.api.box
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.maxWidth
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.instruction.fr

@Adaptive
fun container(
    state: IntroState,
    title: String,
    @Adaptive content: () -> Unit
) {
    grid {
        mobileScreen .. rowTemplate(213.dp, 1.fr, 81.dp)

        title(title)

        column {
            backgroundColor(0x00ff00)
            content()
        }

        grid {
            colTemplate(1.fr, 1.fr) .. maxWidth .. gap(16.dp)

            button("previous") .. maxWidth .. onClick { state.previous() }
            button("next") .. maxWidth .. onClick { state.next() }
        }
    }
}