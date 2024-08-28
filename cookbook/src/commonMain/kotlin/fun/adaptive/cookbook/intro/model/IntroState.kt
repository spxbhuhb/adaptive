package `fun`.adaptive.cookbook.intro.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.store.replaceWith

@Adat
class IntroState(
    val screen: IntroScreen
) {

    fun previous() {
        if (screen == IntroScreen.Start) return
        replaceWith(IntroState(IntroScreen.entries[screen.ordinal-1]))
    }

    fun next() {
        if (screen == IntroScreen.End) return
        replaceWith(IntroState(IntroScreen.entries[screen.ordinal+1]))
    }

}