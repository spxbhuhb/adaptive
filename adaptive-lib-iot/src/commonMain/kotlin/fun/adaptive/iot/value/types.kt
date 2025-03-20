package `fun`.adaptive.iot.value

import `fun`.adaptive.utility.UUID

typealias AioValueId = UUID<AioValue>
typealias AioValueSubscriptionId = UUID<AioValueSubscription>
typealias AioComputeFun<T> = AioValueWorker.WorkerComputeContext.() -> T