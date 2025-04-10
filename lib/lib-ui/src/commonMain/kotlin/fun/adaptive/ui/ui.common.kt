package `fun`.adaptive.ui

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.ui.builtin.commonMainStringsStringStore0
import `fun`.adaptive.ui.snackbar.SnackType
import `fun`.adaptive.wireformat.WireFormatRegistry

suspend fun uiCommon() {
    commonMainStringsStringStore0.load()

    val r = WireFormatRegistry

    r += SnackType
}

fun AdaptiveAdapter.uiCommon() {
    fragmentFactory += arrayOf(LibFragmentFactory)
}