package `fun`.adaptive.ui.form.platform

import `fun`.adaptive.auto.api.AutoItemListener
import `fun`.adaptive.auto.model.ItemId
import `fun`.adaptive.ui.app.basic.BasicAppData
import `fun`.adaptive.ui.navigation.NavState
import kotlinx.browser.window
import org.w3c.dom.PopStateEvent

class BrowserHistoryStateListener(
    val appData: BasicAppData
) : AutoItemListener<NavState>() {

    var popState: NavState? = null

    init {
        window.addEventListener("popstate", { event ->
            event as PopStateEvent
            popState = NavState.parse(event.state.toString())
            appData.navState.update(popState !!)
        })

        appData.navState.update(NavState.parse(window.location.href))
        appData.navState.addListener(this)
    }

    override fun onChange(itemId: ItemId, newValue: NavState, oldValue: NavState?) {
        if (popState == newValue) return // change right after the popstate should not push the state

        val oldUrl = oldValue?.toUrl()
        val newUrl = newValue.toUrl()
        if (newUrl == oldUrl) return

        window.history.pushState(newUrl, newValue.title ?: appData.appName ?: "", newUrl)
    }

}