package `fun`.adaptive.app.platform

import `fun`.adaptive.adat.encodeToJsonString
import `fun`.adaptive.app.BrowserApplication
import `fun`.adaptive.ui.navigation.NavState
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.PopStateEvent

/**
 * Contains the current navigation state of the application and listens for the
 * `popstate` browser event. When `popstate` happens, updates [BrowserApplication.navState].
 */
class BrowserHistoryStateListener(
    val application: BrowserApplication<*>
) {

    init {
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            application.navState.value = NavState.fromJson(event.state.toString().encodeToByteArray()).also {
                document.title = it.title ?: ""
            }
        })
    }

    /**
     * Change the navigation state of the application.
     *
     * - adds a [newState] to the browser history if it is different than he current one
     * - sets the document title from the state
     * - sets [navState]
     */
    fun push(newState: NavState) {
        if (application.navState.value == newState) return // change right after the popstate should not push the state

        val title = newState.title ?: ""
        document.title = title

        window.history.pushState(newState.encodeToJsonString(), newState.title ?: "", newState.url.toString())
        application.navState.value = newState
    }

}