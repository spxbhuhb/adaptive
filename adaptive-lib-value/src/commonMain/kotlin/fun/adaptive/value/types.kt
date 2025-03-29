package `fun`.adaptive.value

import `fun`.adaptive.utility.UUID
import `fun`.adaptive.value.store.AvComputeContext

typealias AvValueId = UUID<AvValue>
typealias AvValueSubscriptionId = UUID<AvSubscription>
typealias AvComputeFun<T> = AvComputeContext.() -> T