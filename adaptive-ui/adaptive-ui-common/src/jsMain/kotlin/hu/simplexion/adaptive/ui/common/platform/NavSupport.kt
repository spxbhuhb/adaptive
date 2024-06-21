/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hu.simplexion.adaptive.ui.common.platform

import hu.simplexion.adaptive.ui.common.CommonAdapter
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.dom.set

class NavSupport(
    val adapter: CommonAdapter
) {

    val lastCounterKey = "adaptive-nav-counter"
    val lastShownKey = "adaptive-nav-last-shown"

    var pendingModificationsEnabled = false
    var pendingModifications = false
    var pendingModificationsConfirm: suspend () -> Boolean = { true }

    fun start() {
        window.addEventListener("popstate", ::onPopState)

        val current = window.history.state?.toString() ?: ""
        if (current.isEmpty()) {
            window.history.replaceState(incrementNavCounter(), "")
        } else {
            window.sessionStorage[lastShownKey] = current
        }

        // FIXME browserOpen(window.location.pathname, window.location.search, window.location.hash)
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

            val path = decodeURIComponent(window.location.pathname)

            adapter.trace("nav-pop-state", path)


            // FIXME browserOpen(path, window.location.search, window.location.hash, false)
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
}