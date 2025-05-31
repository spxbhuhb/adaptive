package `fun`.adaptive.ui.popup

import `fun`.adaptive.foundation.AdaptiveAdapter
import `fun`.adaptive.foundation.AdaptiveFragment
import `fun`.adaptive.foundation.NonAdaptive
import `fun`.adaptive.foundation.instruction.instructionsOf
import `fun`.adaptive.runtime.FrontendWorkspace
import `fun`.adaptive.ui.api.popupAlign

@NonAdaptive
fun <T : Any> dialog(
    adapter: AdaptiveAdapter,
    data: T,
    content: (data: T, hide: () -> Unit) -> Any
) {
    @Suppress("UNCHECKED_CAST") // see comments at dialogHack
    dialogHack(adapter, data, content as (AdaptiveFragment, Int) -> AdaptiveFragment)
}

@NonAdaptive
fun <T : Any> dialog(
    workspace: FrontendWorkspace,
    data: T,
    content: (data: T, hide: () -> Unit) -> Any
) {
    dialog(workspace.frontend, data, content)
}

/**
 * Theoretically using this function directly should work. However, the JS IR linker
 * does not find the function if
 *
 * - the function is in a different module and
 * - the IR compiler plugin changes the signature
 *
 * KT-75416 KJS / IC: "IrLinkageError: Constructor can not be called: No constructor found for symbol" on jsBrowserDevelopmentRun restart
 */
private fun dialogHack(
    adapter: AdaptiveAdapter,
    data: Any,
    content: (parent: AdaptiveFragment, declarationIndex: Int) -> AdaptiveFragment,
) {
    adapter
        .actualize("aui:dialogpopup", adapter.rootFragment, - 1, 3)
        .also {
            it.setStateVariable(0, instructionsOf(popupAlign.absoluteCenter(modal = true)))
            it.setStateVariable(1, data)
            it.setStateVariable(2, content)
            it.create()
            it.mount()
        }
}

