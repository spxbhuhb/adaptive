package `fun`.adaptive.iot

import `fun`.adaptive.foundation.fragment.FoundationFragmentFactory
import `fun`.adaptive.iot.space.ui.wsSpaceBrowserContent
import `fun`.adaptive.iot.space.ui.wsSpaceContentPane
import `fun`.adaptive.iot.space.ui.wsSpaceTool
import `fun`.adaptive.iot.ws.AioWsContext

object IotFragmentFactory : FoundationFragmentFactory() {
    init {
        add(AioWsContext.WSPANE_SPACE_TOOL, ::wsSpaceTool)
        add(AioWsContext.WSPANE_SPACE_CONTENT, ::wsSpaceContentPane)

        //add(AioWsContext.WSPANE_INFRASTRUCTURE_TOOL, ::wsInfrastructureEditorPane)

        add(AioWsContext.WSPANE_MEASUREMENT_LOCATION_CONTENT, ::wsSpaceBrowserContent)
    }
}