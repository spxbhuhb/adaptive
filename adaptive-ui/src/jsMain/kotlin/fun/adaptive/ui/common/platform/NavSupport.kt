/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package `fun`.adaptive.ui.common.platform

import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.ui.common.CommonAdapter
import `fun`.adaptive.ui.common.support.navigation.AbstractNavSupport
import `fun`.adaptive.ui.common.support.navigation.NavData
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.dom.set
import web.url.URLSearchParams

class NavSupport(
    val adapter: CommonAdapter
) : AbstractNavSupport() {

    val lastCounterKey = "adaptive-nav-counter"
    val lastShownKey = "adaptive-nav-last-shown"

    var pendingModificationsEnabled = false
    var pendingModifications = false
    var pendingModificationsConfirm: suspend () -> Boolean = { true }

    override var root = parseLocation()

    fun start() {
        window.addEventListener("popstate", ::onPopState)

        val current = window.history.state?.toString() ?: ""
        if (current.isEmpty()) {
            window.history.replaceState(incrementNavCounter(), "")
        } else {
            window.sessionStorage[lastShownKey] = current
        }
    }

    /**
     * Called when:
     *  - the user clicks on the "Back" button
     *  - the user clicks on the "Forward" button
     *  - the [back] function is called
     *  - direct call to window.history.back
     *  - direct call to window.history.forward
     */
    @Suppress("UNUSED_PARAMETER")
    fun onPopState(event: Event) {

        CoroutineScope(adapter.dispatcher).launch {

            if (stopNavigationOnPending()) return@launch

            val current = (window.history.state as? Int) ?: 0
            window.sessionStorage[lastShownKey] = current.toString()

            root = parseLocation()
            adapter.navChange()
        }
    }

    suspend fun stopNavigationOnPending(): Boolean {
        if (pendingModificationsEnabled && pendingModifications) {

            val isConfirmed = pendingModificationsConfirm()

            if (isConfirmed) {
                pendingModifications = false
            }

            return ! isConfirmed
        }
        return false
    }

    fun forward() = window.history.forward()

    fun back() = window.history.back()

    fun incrementNavCounter(): Int {
        val navCounter = (window.sessionStorage[lastCounterKey]?.toInt() ?: window.history.length) + 1
        window.sessionStorage[lastCounterKey] = navCounter.toString()
        window.sessionStorage[lastShownKey] = navCounter.toString()
        return navCounter
    }

    fun parseLocation(): NavData {
        val params = URLSearchParams(window.location.search)

        val navParams: MutableMap<String, Array<out String>> = mutableMapOf()

        for (key in params.keys()) {
            navParams[key] = params.getAll(key)
        }

        return NavData(
            decodeURIComponent(window.location.pathname).trim('/').split("/"),
            navParams,
            window.location.hash
        )
    }

    override fun segmentChange(owner: AdaptiveFragment, segment: String?) {
        window.history.replaceState(incrementNavCounter(), "", if (segment == null) "" else "/$segment")
    }
}