package `fun`.adaptive.ui.mpw

import `fun`.adaptive.ui.mpw.model.PaneContentItem
import `fun`.adaptive.ui.mpw.model.PaneContentType
import `fun`.adaptive.ui.navigation.NavState
import `fun`.adaptive.utility.Url

/**
 * Maps between [NavState] and MPW items.
 */
interface MultiPaneUrlResolver {

    /**
     * Resolves a navigation state into a workspace item.
     *
     * @return  The resolved item to load into a content pane or null if the resolver cannot resolve the item.
     */
    fun resolve(url: Url) : Pair<PaneContentType, PaneContentItem>?

    /**
     * Converts the given item into a corresponding navigation state.
     *
     * @param item The item to be converted into a NavState.
     *
     * @return The resulting [NavState] if the conversion is successful, or null if it cannot be converted.
     */
    fun toNavState(type: PaneContentType, item : PaneContentItem) : NavState?

}