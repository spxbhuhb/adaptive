package `fun`.adaptive.cookbook.intro.screens

import `fun`.adaptive.cookbook.intro.components.container
import `fun`.adaptive.cookbook.intro.model.IntroState
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.flowText
import `fun`.adaptive.ui.api.maxWidth

@Adaptive
fun end(state : IntroState) {
   container(state, "The End") {
       flowText("For more information visit https://adaptive.fun or https://github.com/spxbhuhb/adaptive") ..
           maxWidth
   }
}