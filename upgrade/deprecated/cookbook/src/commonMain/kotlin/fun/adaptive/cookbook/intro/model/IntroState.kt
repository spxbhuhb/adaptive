package `fun`.adaptive.cookbook.intro.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.update

@Adat
class IntroState(
    val screen: IntroScreen
) {

    fun previous() {
        if (screen == IntroScreen.Start) return
        update(IntroState(IntroScreen.entries[screen.ordinal-1]))
    }

    fun next() {
        if (screen == IntroScreen.End) return
        update(IntroState(IntroScreen.entries[screen.ordinal+1]))
    }

}