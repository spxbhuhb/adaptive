package `fun`.adaptive.cookbook.nav

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.auto.api.autoInstance
import `fun`.adaptive.cookbook.shared.button
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text

@Adat
class NavState(
    val segments: List<String> = emptyList(),
    val parameters: Map<String, String> = emptyMap()
) {
    fun goto(path: String) {
        val store = adatContext?.store
        checkNotNull(store) { "no store for the nav state" }
        store.update(this, arrayOf("segments"), path.split("/").mapNotNull { it.ifEmpty { null } })
    }
}

val sharedNavState = autoInstance(NavState())

val options = listOf("option1", "option2", "option3")

@Adaptive
fun navMain() {
    val nav = autoInstance(sharedNavState)

    column {
        button("Option 1") .. onClick { nav?.goto(options[0]) }
        button("Option 2") .. onClick { nav?.goto(options[1]) }
        button("Option 3") .. onClick { nav?.goto(options[2]) }

        when (nav?.segments?.firstOrNull()) {
            options[0] -> text("navigated to option 1")
            options[1] -> text("navigated to option 2")
            options[2] -> text("navigated to option 3")
        }
    }


}