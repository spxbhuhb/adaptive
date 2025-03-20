package `fun`.adaptive.iot.value.operation

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.iot.value.AioComputeFun
import `fun`.adaptive.iot.value.AioValueSubscription
import `fun`.adaptive.iot.value.AioValueWorker

@Adat
class AvoComputation<T>(): AioValueOperation() {

    var computation : AioComputeFun<T>? = null

}