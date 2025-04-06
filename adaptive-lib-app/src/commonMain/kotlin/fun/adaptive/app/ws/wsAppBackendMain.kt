package `fun`.adaptive.app.ws

import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.value.app.valueClientBackend

@Adaptive
fun wsAppBackendMain(): AdaptiveFragment {

    worker { SnackbarManager() }
    valueClientBackend("general") // FIXME "general" value domain

    return fragment()
}