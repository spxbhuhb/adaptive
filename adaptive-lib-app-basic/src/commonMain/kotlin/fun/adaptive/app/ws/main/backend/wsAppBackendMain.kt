package `fun`.adaptive.app.ws.main.backend

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.value.app.valueClientBackend

@Adaptive
fun wsAppBackendMain(): AdaptiveFragment {

    worker { SnackbarManager() }
    valueClientBackend()

    return fragment()
}