package `fun`.adaptive.iot.infrastructure

import `fun`.adaptive.iot.ws.AioWsContext
import `fun`.adaptive.ui.tree.TreeViewModel
import `fun`.adaptive.utility.UUID

typealias InfrastructureTreeModel = TreeViewModel<AioInfrastructureItem, AioWsContext>
typealias AioInfrastructureItemId = UUID<AioInfrastructureItem>
