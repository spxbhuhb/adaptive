package `fun`.adaptive.app.platform
//
//import `fun`.adaptive.app.sidebar.ui.BasicAppData
//import `fun`.adaptive.auto.api.AutoItemListener
//import `fun`.adaptive.auto.model.ItemId
//import `fun`.adaptive.ui.navigation.NavState
//import `fun`.adaptive.wireformat.toJson
//import kotlinx.browser.document
//import kotlinx.browser.window
//import org.w3c.dom.PopStateEvent
//
//class BrowserHistoryStateListener(
//    val appData: BasicAppData
//) : AutoItemListener<NavState>() {
//
//    var popState: NavState? = null
//
//    init {
//        window.addEventListener("popstate", { event ->
//            event as PopStateEvent
//            popState = NavState.Companion.fromJson(event.state.toString().encodeToByteArray())
//
//            val title = popState?.title ?: appData.appName ?: ""
//            document.title = title
//
//            appData.navState.update(popState !!)
//        })
//
//        appData.navState.update(NavState.Companion.parse(window.location.href))
//        appData.navState.addListener(this)
//    }
//
//    override fun onChange(itemId: ItemId, newValue: NavState, oldValue: NavState?) {
//        if (popState == newValue) return // change right after the popstate should not push the state
//        if (oldValue == newValue) return
//
//        val title = newValue.title ?: appData.appName ?: ""
//        document.title = title
//
//        window.history.pushState(newValue.toJson(NavState.Companion).decodeToString(), newValue.title ?: "", newValue.toUrl())
//    }
//
//}