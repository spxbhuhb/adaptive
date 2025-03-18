package `fun`.adaptive.iot.space.model

import `fun`.adaptive.general.SelfObservable
import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.tree.TreeViewModel

class SpaceBrowserState(
    val config: SpaceBrowserConfig
) : SelfObservable<SpaceBrowserState>() {

    var tree: TreeViewModel<SpaceBrowserWsItem, AioWsContext>? = null

}