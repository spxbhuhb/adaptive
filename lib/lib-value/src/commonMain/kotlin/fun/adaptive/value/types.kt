package `fun`.adaptive.value

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.store.AvComputeContext

typealias AvValueDomain = String
typealias AvValueId = UUID<AvValue<*>>
typealias AvSubscriptionId = UUID<AvSubscription>
typealias AvComputeFun<T> = AvComputeContext.() -> T

typealias FriendlyItemId = String
typealias AvMarker = String
typealias AvStatus = String
typealias AvRefLabel = String