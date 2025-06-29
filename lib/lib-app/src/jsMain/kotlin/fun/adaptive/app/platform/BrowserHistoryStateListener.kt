package `fun`.adaptive.app.platform

import `fun`.adaptive.adat.encodeToJsonString
import `fun`.adaptive.app.BrowserApplication
import `fun`.adaptive.ui.navigation.NavState
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.PopStateEvent

class BrowserHistoryStateListener(
    val application: BrowserApplication<*>
) {

    var popState: NavState? = null

    init {
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            popState = NavState.Companion.fromJson(event.state.toString().encodeToByteArray())

            val title = popState?.title ?: ""
            document.title = title
        })
    }

    fun push(newValue: NavState, oldValue: NavState?) {
        if (popState == newValue) return // change right after the popstate should not push the state
        if (oldValue == newValue) return

        val title = newValue.title ?: ""
        document.title = title

        window.history.pushState(newValue.encodeToJsonString(), newValue.title ?: "", newValue.url.toString())
    }

}