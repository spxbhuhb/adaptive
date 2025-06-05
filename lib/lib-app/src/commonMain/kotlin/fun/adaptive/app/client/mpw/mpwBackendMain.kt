package `fun`.adaptive.app.client.mpw

import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.fragment
import `fun`.adaptive.ui.snackbar.SnackbarManager
import `fun`.adaptive.value.app.valueClientBackend

@Adaptive
fun mpwBackendMain(): AdaptiveFragment {

    worker { SnackbarManager() }
    valueClientBackend()

    return fragment()
}