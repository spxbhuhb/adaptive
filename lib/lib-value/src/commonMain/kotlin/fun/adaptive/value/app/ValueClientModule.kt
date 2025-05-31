package `fun`.adaptive.value.app

import `fun`.adaptive.runtime.BackendWorkspace
import `fun`.adaptive.runtime.FrontendWorkspace

class ValueClientModule<FW : FrontendWorkspace, BW : BackendWorkspace> : ValueModule<FW, BW>()