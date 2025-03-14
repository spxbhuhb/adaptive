package `fun`.adaptive.cookbook.ui.navigation

import `fun`.adaptive.auto.api.autoItem
import `fun`.adaptive.cookbook.appData
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.onClick
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.instruction.dp
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.ui.navigation.navState

val navRouting = navState("navigation")

private val option1 = NavState(listOf("navigation", "1"))
private val option2 = NavState(listOf("navigation", "2"))

@Adaptive
fun navigationRecipe(): AdaptiveFragment {

    val navState = autoItem(appData.navState)

    when (navState) {
        in option1 -> content("current place: option 1", navState)
        in option2 -> content("current place: option 2", navState)
        else -> content("current place: no option selected", navState)
    }

    return fragment()
}

@Adaptive
private fun content(message: String, navState: NavState?) {
    column {
        gap { 16.dp }

        text(message + " " + navState?.parameters?.get("sub"))
        button("option 1.1") .. onClick { navState?.goto(NavState(listOf("navigation", "1"), parameters = mapOf("sub" to "1.1"))) }
        button("option 1.2") .. onClick { navState?.goto(NavState(listOf("navigation", "1"), parameters = mapOf("sub" to "1.2"))) }
        button("option 2.1") .. onClick { navState?.goto(NavState(listOf("navigation", "2"), parameters = mapOf("sub" to "2.1"))) }
        button("option 2.2") .. onClick { navState?.goto(NavState(listOf("navigation", "2"), parameters = mapOf("sub" to "2.2"))) }
    }
}