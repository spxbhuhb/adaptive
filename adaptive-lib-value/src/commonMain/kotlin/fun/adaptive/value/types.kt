package `fun`.adaptive.value

import `fun`.adaptive.utility.UUID

typealias AvValueId = UUID<AvValue>
typealias AvValueSubscriptionId = UUID<AvSubscription>
typealias AvComputeFun<T> = AvValueWorker.WorkerComputeContext.() -> T