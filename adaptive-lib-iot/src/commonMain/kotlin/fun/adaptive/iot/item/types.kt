package `fun`.adaptive.iot.item

import `fun`.adaptive.iot.value.AioValueId
import `fun`.adaptive.utility.UUID

typealias AioItemId = UUID<AioItem>
typealias FriendlyItemId = Int
typealias AioMarker = String
typealias AioMarkerMap = Map<AioMarker, AioValueId?>